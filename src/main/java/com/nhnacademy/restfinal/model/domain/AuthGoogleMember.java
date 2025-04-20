package com.nhnacademy.restfinal.model.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AuthGoogleMember implements OAuth2User {

    private String memberId;
    private String role;
    private Map<String, Object> attributes;

    public AuthGoogleMember(Member member, Map<String, Object> attributes) {
        this.memberId = memberId;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + this.role;

        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return memberId;
    }
}
