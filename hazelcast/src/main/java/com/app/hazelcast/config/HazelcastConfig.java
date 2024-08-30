package com.app.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.spring.cache.HazelcastCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HazelcastConfig {

  @Bean
  public Config getConfig() {
    Config config = new Config();
    config
        .setInstanceName("user-app-hazelcast-cache")
        .addMapConfig(
            new MapConfig()
                .setName(CacheConstant.CACHE_NAME)
                .setEvictionConfig(
                    new EvictionConfig()
                        .setSize(1000)
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                        .setEvictionPolicy(EvictionPolicy.LRU))
                .setTimeToLiveSeconds(Integer.MAX_VALUE)
                .setMaxIdleSeconds(0));

    return config;
  }

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    IMap<Object, Object> map = getHazelcastInstance().getMap(CacheConstant.CACHE_NAME);
    Cache cache = new HazelcastCache(map);
    simpleCacheManager.setCaches(List.of(cache));
    return simpleCacheManager;
  }

  @Bean
  public HazelcastInstance getHazelcastInstance() {
    Config config = getConfig();
    config.getNetworkConfig().getRestApiConfig().setEnabled(true);
    JoinConfig joinConfig = config.getNetworkConfig().getJoin();
    joinConfig.getMulticastConfig().setEnabled(false);
    joinConfig.getTcpIpConfig().addMember("localhost").setEnabled(true);
    return Hazelcast.newHazelcastInstance(config);
  }
  public static class CacheConstant{
    public static final String CACHE_NAME = "userCache";
  }
}
