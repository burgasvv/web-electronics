package org.burgas.webelectronics.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.burgas.webelectronics.entity.category.Category
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisCategoryTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper)
    : RedisTemplate<String, Category> {
        return RedisTemplate<String, Category>().apply {
            this.connectionFactory = redisConnectionFactory
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = Jackson2JsonRedisSerializer(objectMapper, Category::class.java)
            this.afterPropertiesSet()
        }
    }
}