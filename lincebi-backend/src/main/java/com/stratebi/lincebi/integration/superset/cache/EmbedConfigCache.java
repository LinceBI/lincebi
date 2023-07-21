package com.stratebi.lincebi.integration.superset.cache;

import com.stratebi.lincebi.integration.superset.model.GuestToken;
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

	public final Cache<String, GuestToken> cache;
	public final CacheManager cacheManager;
	public final CacheConfiguration<String, GuestToken> cacheConfiguration;
	public final ResourcePools resourcePools;
	public final ExpiryPolicy<String, GuestToken> expiryPolicy;

	private EmbedConfigCache() {
		this.expiryPolicy = new EmbedConfigExpiryPolicy<String, GuestToken>();

		this.resourcePools = ResourcePoolsBuilder
			.heap(512)
			.build();

		this.cacheConfiguration = CacheConfigurationBuilder
			.newCacheConfigurationBuilder(String.class, GuestToken.class, this.resourcePools)
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

class EmbedConfigExpiryPolicy<K, V> implements ExpiryPolicy<String, GuestToken> {

	@Override
	public Duration getExpiryForAccess(String key, Supplier<? extends GuestToken> value) {
		return null;
	}

	@Override
	public Duration getExpiryForCreation(String key, GuestToken value) {
		return Duration.between(Instant.now(), Instant.now().plus(Duration.ofSeconds(value.getExpSec())));
	}

	@Override
	public Duration getExpiryForUpdate(String key, Supplier<? extends GuestToken> oldValue, GuestToken newValue) {
		return Duration.between(Instant.now(), Instant.now().plus(Duration.ofSeconds(newValue.getExpSec())));
	}

}
