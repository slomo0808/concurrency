package top.slomo.concurrency.example.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Bean(name = "redisPool")
    public JedisPool jedisPool(
            @Value("${jedis.host}") String host,
            @Value("${jedis.port}") Integer port,
            @Value("${jedis.auth}") String auth) {
        return new JedisPool(new GenericObjectPoolConfig(), host, port, 2000, auth);
    }
}
