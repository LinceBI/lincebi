package com.stratebi.lincebi.filemetadata.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;

public class FileMetadataCache {

	private static volatile FileMetadataCache instance;

	public final Cache<String, String> cache;
	public final CacheManager cacheManager;
	public final CacheConfiguration<String, String> cacheConfiguration;
	public final ResourcePools resourcePools;
	public final ExpiryPolicy<Object, Object> expiryPolicy;

	private FileMetadataCache() {
		this.expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(10));

		this.resourcePools = ResourcePoolsBuilder
			.heap(16)
			.build();

		this.cacheConfiguration = CacheConfigurationBuilder
			.newCacheConfigurationBuilder(String.class, String.class, this.resourcePools)
			.withExpiry(this.expiryPolicy)
			.build();

		this.cacheManager = CacheManagerBuilder
			.newCacheManagerBuilder()
			.build();

		cacheManager.init();

		this.cache = this.cacheManager.createCache("Default", this.cacheConfiguration);
	}

	public static FileMetadataCache getInstance() {
		if (instance == null) {
			synchronized (FileMetadataCache.class) {
				if (instance == null) instance = new FileMetadataCache();
			}
		}

		return instance;
	}

}
