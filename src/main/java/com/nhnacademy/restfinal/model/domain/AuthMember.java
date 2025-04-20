package com.nhnacademy.restfinal.model.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class AuthMember implements UserDetails {
    private String memberId;
    private String role;
    private String password;

    public AuthMember(Member member) {
        this.memberId = member.getId();
        this.role = member.getRole().name();
        this.password = member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + this.role;

        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

}

