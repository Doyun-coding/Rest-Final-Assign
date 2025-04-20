package com.nhnacademy.restfinal.service;

import com.nhnacademy.restfinal.exception.NotFoundMemberException;
import com.nhnacademy.restfinal.exception.TTLMemberException;
import com.nhnacademy.restfinal.model.domain.AuthMember;
import com.nhnacademy.restfinal.model.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final int MAX_ATTEMPTS = 5;
    private static final String LOGIN_FAIL = "LOGIN_FAIL:";

    @Autowired
    private MemberService memberService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DoorayService doorayService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.getMember(username);
        if(member == null) {
            throw new NotFoundMemberException("해당하는 사용자가 없습니다.");
        }

        log.info("memberId:{}, memberPassword:{}", member.getId(), member.getPassword());

        String key = LOGIN_FAIL + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        if(Objects.nonNull(attempts) && attempts >= MAX_ATTEMPTS) {
            doorayService.sendMessage(username);

            throw new TTLMemberException("해당 사용자는 로그인 차단 상태입니다.");
        }

        return new AuthMember(member);
    }

}
