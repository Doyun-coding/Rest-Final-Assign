package com.nhnacademy.restfinalassign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;

    private static final Duration TTL = Duration.ofMinutes(1);
    private static final String LOGIN_FAIL_KEY = "Logout:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void fail(String username) {
        String key = LOGIN_FAIL_KEY + "" + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);

        if(Objects.isNull(attempts) || attempts == 0) {
            attempts = 0;
        }

        attempts++;
        redisTemplate.opsForValue().set(key, attempts, TTL);

        if(attempts >= MAX_ATTEMPTS) {
            System.err.println("[로그인 차단] 사용자: " + username);
        }
        else {
            System.err.println("[로그인 실패] 사용자: " + username + " " + attempts + "회");
        }

    }

    public void success(String username) {
        redisTemplate.delete(LOGIN_FAIL_KEY + "" + username);
    }

}
