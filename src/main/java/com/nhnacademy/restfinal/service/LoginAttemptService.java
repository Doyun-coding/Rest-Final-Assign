package com.nhnacademy.restfinal.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  TTL은 Time to Live로 데이터가 유효한 시간 또는 생존 시간을 의미한다
 *  REDIS 나 DB 에서 TTL 은 데이터에 유효 시간을 설정하는 값으로 일정 시간이 지나면 자동으로 삭제됩니다.
 *  밑에서 설정한 1분이 지나면 Redis 에 있는 해당 사용자의 Attempts 값이 삭제된다
 */
@Slf4j
@Service
public class LoginAttemptService {

    private static final String LOGIN_FAIL = "LOGIN_FAIL:";
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration TTL = Duration.ofMinutes(1);

    @Autowired
    private DoorayService doorayService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void fail(String username) {
        String key = LOGIN_FAIL + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(attempts)) {
            attempts = 0;
        }

        if(attempts < MAX_ATTEMPTS) {
            attempts++;
            redisTemplate.opsForValue().set(key, attempts);

            if(attempts >= MAX_ATTEMPTS) {
                redisTemplate.opsForValue().set(key, attempts, TTL);

                doorayService.sendMessage(username);

                log.info("사용자 '" + username + "' 로그인 차단됨 (1분)");
            }
            else {
                log.info("로그인 실패: " + username + " (" + attempts + "/" + MAX_ATTEMPTS + ")");
            }

        }
    }

    public void success(String username) {
        String key = LOGIN_FAIL + username;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String sessionId = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("SESSIONID")) {
                    sessionId = cookie.getValue();
                    redisTemplate.delete(sessionId); // 세션 삭제

                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                if(cookie.getName().equals("JWT_TOKEN")) {
                    sessionId = cookie.getValue();
                    redisTemplate.delete(sessionId); // 세션 삭제

                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        redisTemplate.delete(key);
    }

//    public boolean isBlocked(String username) {
//        String key = LOGIN_FAIL + username;
//        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
//
//        return attempts != null && attempts >= MAX_ATTEMPTS;
//    }

//    private static final String LOGIN_FAIL = "LOGIN_FAIL:";
//    private final int MAX_ATTEMPTS = 5;
//    private Map<String, Integer> attemptsCache = new HashMap<>();
//
//    public void fail(String username) {
//        int attempts = attemptsCache.getOrDefault(username, 0);
//        attempts++;
//        attemptsCache.put(username, attempts);
//
//        if(attempts >= MAX_ATTEMPTS) {
//
//        }
//
//    }
//
//    public void success(String username) {
//        attemptsCache.remove(username);
//    }

}
