package com.nhnacademy.restfinalassign.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ObjectMapper redisObjectMapper() {
        return new ObjectMapper();
    }

    /***
     *
     * @param redisConnectionFactory
     * @param objectMapper : ObjectMapper는 Java 객체를 JSON으로 직렬화하거나, JSON을 JAVA 객체로 역직렬화할 때 사용하는 Jackson 라이브러리 핵심 도구이다
     *                     Redis나 HTTP API에서 JSON 변환할 때 ObjectMapper가 필수로 사용된다
     *                     Redis에서 데이터를 저장할 때 객체를 JSON으로 변환하고, 꺼낼 때 JSON을 객체로 변환하려면 ObjectMapper가 필요하다
     *
     *                     보통은 Spring Boot가 자동으로 내부에서 사용하는데,
     *                     직접 RedisTemplate의 Serilizer을 설정하거나 커스터마이징할 때 사용자가 명시적으로 주입할 수 있도록 Bean 등록하는 것이다
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();
        sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);
        sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
        sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return sessionRedisTemplate;
    }

}
