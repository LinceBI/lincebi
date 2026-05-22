package com.stratebi.lincebi.filemetadata.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.ExpiryPolicy;
import com.stratebi.lincebi.util.EnvUtils;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileMetadataCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataCache.class);

	private static final Duration FRESH_TTL = EnvUtils.parseTtlSecondsEnv("LINCEBI_METADATA_FRESH_TTL", Duration.ofSeconds(300));
	private static final Duration STALE_TTL = EnvUtils.parseTtlSecondsEnv("LINCEBI_METADATA_STALE_TTL", Duration.ofSeconds(604800));
	private static final long HEAP_SIZE_MB = EnvUtils.parseLongEnv("LINCEBI_METADATA_HEAP_SIZE_MB", 256);

	private static volatile FileMetadataCache instance;

	private final Cache<String, CacheEntry> cache;
	private final CacheManager cacheManager;
	private final ExecutorService refreshExecutor;
	private final ConcurrentMap<String, Boolean> inFlight;
	private final AtomicLong generation;
	private final ConcurrentMap<String, AtomicLong> userGenerations;

	private FileMetadataCache() {
		ExpiryPolicy<Object, Object> expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(FileMetadataCache.STALE_TTL);

		ResourcePools resourcePools = ResourcePoolsBuilder
			.newResourcePoolsBuilder()
			.heap(FileMetadataCache.HEAP_SIZE_MB, MemoryUnit.MB)
			.build();

		CacheConfiguration<String, CacheEntry> cacheConfiguration = CacheConfigurationBuilder
			.newCacheConfigurationBuilder(String.class, CacheEntry.class, resourcePools)
			.withExpiry(expiryPolicy)
			.build();

		this.cacheManager = CacheManagerBuilder
			.newCacheManagerBuilder()
			.build();

		this.cacheManager.init();

		this.cache = this.cacheManager.createCache("Default", cacheConfiguration);

		this.refreshExecutor = Executors.newSingleThreadExecutor(runnable -> {
			Thread thread = new Thread(runnable, "FileMetadataCache-Refresh");
			thread.setDaemon(true);
			return thread;
		});

		this.inFlight = new ConcurrentHashMap<>();
		this.generation = new AtomicLong(0);
		this.userGenerations = new ConcurrentHashMap<>();
	}

	private long userGen(String userName) {
		return this.userGenerations.computeIfAbsent(userName, k -> new AtomicLong(0)).get();
	}

	private void bumpUserGen(String userName) {
		this.userGenerations.computeIfAbsent(userName, k -> new AtomicLong(0)).incrementAndGet();
	}

	public static FileMetadataCache getInstance() {
		if (instance == null) {
			synchronized (FileMetadataCache.class) {
				if (instance == null) instance = new FileMetadataCache();
			}
		}

		return instance;
	}

	public String getOrCompute(String key, String userName, boolean bypass, Supplier<String> supplier) {
		if (!bypass) {
			CacheEntry entry = this.cache.get(key);
			if (entry != null && entry.getUserGen() == this.userGen(userName)) {
				if (System.currentTimeMillis() - entry.getComputedAt() >= FileMetadataCache.FRESH_TTL.toMillis()) {
					this.triggerRefresh(key, userName, supplier);
				}
				return FileMetadataCache.decompress(entry.getCompressed());
			}
		}

		long startedGeneration = this.generation.get();
		long startedUserGen = this.userGen(userName);
		String value = supplier.get();
		if (value != null && this.generation.get() == startedGeneration && this.userGen(userName) == startedUserGen) {
			this.cache.put(key, new CacheEntry(FileMetadataCache.compress(value), System.currentTimeMillis(), userName, startedUserGen));
		}
		return value;
	}

	private void triggerRefresh(String key, String userName, Supplier<String> supplier) {
		if (this.inFlight.putIfAbsent(key, Boolean.TRUE) != null) return;

		long startedGeneration = this.generation.get();
		long startedUserGen = this.userGen(userName);
		try {
			this.refreshExecutor.submit(() -> {
				try {
					SecurityHelper.getInstance().runAsUser(userName, () -> {
						String value = supplier.get();
						if (value != null && this.generation.get() == startedGeneration && this.userGen(userName) == startedUserGen) {
							this.cache.put(key, new CacheEntry(FileMetadataCache.compress(value), System.currentTimeMillis(), userName, startedUserGen));
						}
						return null;
					});
				} catch (Exception ex) {
					FileMetadataCache.LOGGER.error("Background refresh failed for key '" + key + "'", ex);
				} finally {
					this.inFlight.remove(key);
				}
			});
		} catch (RejectedExecutionException ex) {
			this.inFlight.remove(key);
			FileMetadataCache.LOGGER.warn("Refresh task rejected for key '" + key + "'");
		}
	}

	public void clear() {
		this.generation.incrementAndGet();
		this.cache.clear();
		this.inFlight.clear();
	}

	public void clear(String userName) {
		this.bumpUserGen(userName);
	}

	public static byte[] compress(String value) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (GZIPOutputStream gzos = new GZIPOutputStream(baos) {
			{ this.def.setLevel(Deflater.BEST_SPEED); }
		}) {
			gzos.write(value.getBytes(StandardCharsets.UTF_8));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return baos.toByteArray();
	}

	public static String decompress(byte[] compressed) {
		try (GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
			return new String(gzis.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static final class CacheEntry implements Serializable {

		private static final long serialVersionUID = 1L;

		private final byte[] compressed;
		private final long computedAt;
		private final String userName;
		private final long userGen;

		public CacheEntry(byte[] compressed, long computedAt, String userName, long userGen) {
			this.compressed = compressed;
			this.computedAt = computedAt;
			this.userName = userName;
			this.userGen = userGen;
		}

		public byte[] getCompressed() {
			return this.compressed;
		}

		public long getComputedAt() {
			return this.computedAt;
		}

		public String getUserName() {
			return this.userName;
		}

		public long getUserGen() {
			return this.userGen;
		}

	}

}
