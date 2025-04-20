package com.nhnacademy.restfinal.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 로그아웃 핸들러 동작 과정
 * SecurityConfig 에서 /logout 요청이 들어오면 스프링 내부에서 CustomLogoutHandler 를 실행시킨다
 * request 에 있는 쿠키들을 모두 가져와서 쿠키의 이름이 SESSIONID 인 쿠키를 찾고 key 값의 value 값을 삭제시키고 cookie 도 초기화해준다
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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

        log.info("로그아웃 완료");
    }

}
