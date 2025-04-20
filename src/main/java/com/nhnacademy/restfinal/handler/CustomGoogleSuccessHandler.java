package com.nhnacademy.restfinal.handler;

import com.nhnacademy.restfinal.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Objects;

public class CustomGoogleSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDIRECT_PATH = "/auth/google";

    private LoginAttemptService loginAttemptService;
    private RedisTemplate<String, Object> redisTemplate;

    public CustomGoogleSuccessHandler(LoginAttemptService loginAttemptService, RedisTemplate<String, Object> redisTemplate) {
        this.loginAttemptService = loginAttemptService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getAttribute("sub");

        if(Objects.nonNull(username)) {
            String sessionId = request.getSession().getId();
            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(60 * 60);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
            redisTemplate.opsForValue().set(sessionId, username);

            loginAttemptService.success(username);

            String redirectUrl = REDIRECT_PATH;

            response.sendRedirect(redirectUrl);
        }
        else {
            response.sendRedirect("/login");
        }

    }

}
