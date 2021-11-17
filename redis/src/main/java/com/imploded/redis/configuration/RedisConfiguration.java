package com.imploded.redis.configuration;

import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shuai.yang
 */
@Configuration
public class RedisConfiguration {
    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, ?> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(factory);
        return template;
    }

    public LettuceConnectionFactory lettuceConnectionFactory() {
        List<String> clusterNodes  = redisProperties.getCluster().getNodes();
        Set<RedisNode> nodes = new HashSet<>();
        clusterNodes .forEach(address -> nodes.add(new RedisNode(address.split(":")[0].trim(), Integer.parseInt(address.split(":")[1].trim()))));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setClusterNodes(nodes);
        redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());

        return new LettuceConnectionFactory(redisClusterConfiguration, getLettucePoolingClientConfiguration(poolConfig));
    }

    private LettucePoolingClientConfiguration getLettucePoolingClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        ClusterTopologyRefreshOptions refreshOptions = ClusterTopologyRefreshOptions.builder()
                // 开启所有自适应刷新
                .enableAllAdaptiveRefreshTriggers()
                // 自适应超时时间
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(25))
                .enablePeriodicRefresh(Duration.ofSeconds(25))
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(10)))
                .topologyRefreshOptions(refreshOptions)
                .build();
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .clientOptions(clusterClientOptions)
                .build();
    }
}
