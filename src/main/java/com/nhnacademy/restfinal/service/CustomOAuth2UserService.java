package com.nhnacademy.restfinal.service;

import com.nhnacademy.restfinal.common.token.JwtToken;
import com.nhnacademy.restfinal.model.domain.Member;
import com.nhnacademy.restfinal.model.request.MemberRequest;
import com.nhnacademy.restfinal.model.type.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String username = oAuth2User.getAttribute("sub");

        Member member = null;
        if (!memberService.isExists(username)) {
            member = new Member(username, "googleUSer", "", 1, Role.GOOGLE);

            memberService.createMember(new MemberRequest(username, "googleUser", "", 1, Role.GOOGLE));
        }
        else {
            member = memberService.getMember(username);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        request.getSession().invalidate(); // 기존 세션 무효화

        String token = JwtToken.generateToken(username);
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

//        String sessionId = request.getSession().getId();
//        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
//        sessionCookie.setHttpOnly(true);
//        sessionCookie.setMaxAge(60 * 60);
//        sessionCookie.setPath("/");
//        response.addCookie(sessionCookie);

        redisTemplate.opsForValue().set(token, username);
        loginAttemptService.success(username);

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_GOOGLE")),
                oAuth2User.getAttributes(),
                "sub"
        );
    }
}
