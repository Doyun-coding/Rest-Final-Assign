package com.nhnacademy.restfinal.common.filter;

import com.nhnacademy.restfinal.common.token.JwtToken;
import com.nhnacademy.restfinal.model.domain.AuthMember;
import com.nhnacademy.restfinal.model.domain.Member;
import com.nhnacademy.restfinal.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

//        Cookie[] cookies = request.getCookies();
//        if(cookies != null) {
//            for(Cookie cookie : cookies) {
//                if(cookie.getName().equals("JWT_TOKEN")) {
//                    String token = cookie.getValue();
//
//                    try {
//                        String username = JwtToken.getUsernameFromToken(token);
//                        Member member = memberService.getMember(username);
//                        AuthMember authMember = new AuthMember(member);
//
//                        Authentication auth = new PreAuthenticatedAuthenticationToken(authMember, null, authMember.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//                    }
//                    catch (Exception e) {
//                        SecurityContextHolder.clearContext();
//                    }
//
//                }
//            }
//        }

        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = JwtToken.getUsernameFromToken(token);

            if (username != null) {
                Member member = memberService.getMember(username);
                AuthMember authMember = new AuthMember(member);
                Authentication auth = new PreAuthenticatedAuthenticationToken(authMember, null, authMember.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
