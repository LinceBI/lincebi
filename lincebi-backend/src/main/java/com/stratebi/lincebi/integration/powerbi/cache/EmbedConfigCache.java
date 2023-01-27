package com.stratebi.lincebi.integration.powerbi.cache;

import com.stratebi.lincebi.integration.powerbi.model.EmbedConfig;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class EmbedConfigCache {

	private static volatile EmbedConfigCache instance;

	public final Cache<String, EmbedConfig> cache;
	public final CacheManager cacheManager;
	public final CacheConfiguration<String, EmbedConfig> cacheConfiguration;
	public final ResourcePools resourcePools;
	public final ExpiryPolicy<String, EmbedConfig> expiryPolicy;

	private EmbedConfigCache() {
		this.expiryPolicy = new EmbedConfigExpiryPolicy<String, EmbedConfig>();

		this.resourcePools = ResourcePoolsBuilder
			.heap(512)
			.build();

		this.cacheConfiguration = CacheConfigurationBuilder
			.newCacheConfigurationBuilder(String.class, EmbedConfig.class, this.resourcePools)
			.withExpiry(this.expiryPolicy)
			.build();

		this.cacheManager = CacheManagerBuilder
			.newCacheManagerBuilder()
			.build();

		cacheManager.init();

		this.cache = this.cacheManager.createCache("Default", this.cacheConfiguration);
	}

	public static EmbedConfigCache getInstance() {
		if (instance == null) {
			synchronized (EmbedConfigCache.class) {
				if (instance == null) instance = new EmbedConfigCache();
			}
		}

		return instance;
	}

}

class EmbedConfigExpiryPolicy<K, V> implements ExpiryPolicy<String, EmbedConfig> {

	@Override
	public Duration getExpiryForAccess(String key, Supplier<? extends EmbedConfig> value) {
		return null;
	}

	@Override
	public Duration getExpiryForCreation(String key, EmbedConfig value) {
		return Duration.between(Instant.now(), Instant.parse(value.getEmbedToken().getExpiration()));
	}

	@Override
	public Duration getExpiryForUpdate(String key, Supplier<? extends EmbedConfig> oldValue, EmbedConfig newValue) {
		return Duration.between(Instant.now(), Instant.parse(newValue.getEmbedToken().getExpiration()));
	}

}
