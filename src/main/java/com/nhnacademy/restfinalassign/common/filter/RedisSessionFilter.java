package com.nhnacademy.restfinalassign.common.filter;

import com.nhnacademy.restfinalassign.model.domain.AuthMember;
import com.nhnacademy.restfinalassign.model.domain.Member;
import com.nhnacademy.restfinalassign.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * RedisSessionFilter는 Spring Security의 인증 흐름에 끼어들어서 쿠키에 저장된 세션 ID를 기반으로 Redis에서 사용자 정보를 찾아 인증을 시켜주는 커스텀 필터이다.
 */
@Component
public class RedisSessionFilter extends OncePerRequestFilter {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /**
         *  SESSIONID 이름을 가진 Cookie 를 찾고 해당 Cookie 의 Value 값인 SessionId를 가져온다
         */
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("SESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        /**
         *  Redis 에 위에서 찾은 해당 SessionID 값에 해당하는 Value 값을 가져온다
         *  Value 값으로 Member: Redis에 저장된 사용자 정보를 가져와서 해당 사용자의 Principal 값과 Credentials 등을 컨트롤러/서비스에서 사용할 수 있게 되었다
         *  PreAuthenticatedAuthenticationToken은 이미 인증된 사용자임을 표현하는 토큰이다
         *  SecurityContextHolder 안에 있는 Context에 사용자의 권한을 저장한다
         */
        if(sessionId != null) {

            Object o = redisTemplate.opsForValue().get(sessionId);
            String username = (String) o;
            if(username != null) {
                Member member = memberService.getMember(username);
                AuthMember authMember = new AuthMember(member);
                Authentication auth = new PreAuthenticatedAuthenticationToken(authMember, null, authMember.getAuthorities());
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(request, response);
    }
}
