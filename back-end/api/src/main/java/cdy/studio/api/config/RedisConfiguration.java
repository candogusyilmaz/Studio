package cdy.studio.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConfiguration {
    @Bean
    public LettuceConnectionFactory cacheConfiguration() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<byte[], byte[]> redisTemplate(RedisConnectionFactory connection) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connection);
        return template;
    }
}
