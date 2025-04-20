package com.nhnacademy.restfinal.handler;

import com.nhnacademy.restfinal.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Map<String, String> REDIRECT_PATH = Map.of(
            "ADMIN", "/auth/admin",
            "admin", "/auth/admin",
            "MEMBER", "/auth/member",
            "member", "/auth/member",
            "ROLE_MEMBER", "/auth/member",
            "ROLE_ADMIN", "/auth/admin"
    );

    private LoginAttemptService loginAttemptService;
    private RedisTemplate<String, Object> redisTemplate;

    public CustomAuthenticationSuccessHandler(LoginAttemptService loginAttemptService, RedisTemplate<String, Object> redisTemplate) {
        this.loginAttemptService = loginAttemptService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        loginAttemptService.success(authentication.getName());

        String sessionId = request.getSession().getId();
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        redisTemplate.opsForValue().set(sessionId, authentication.getName());

        String role = authentication.getAuthorities().iterator().next().getAuthority(); // ROLE 찾기

        log.info("role:{}", role);
        String redirectUrl = REDIRECT_PATH.getOrDefault(role, "/"); // ROLE 에 따라 URL Redirect

        response.sendRedirect(redirectUrl);
        //super.onAuthenticationSuccess(request, response, authentication);

    }

}
