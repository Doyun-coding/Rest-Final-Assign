package com.nhnacademy.restfinalassign.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.restfinalassign.exception.DuplicateResourceException;
import com.nhnacademy.restfinalassign.exception.NotFoundMemberException;
import com.nhnacademy.restfinalassign.model.domain.Member;
import com.nhnacademy.restfinalassign.model.request.MemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String HASH_NAME_MEMBER = "Member:";

    public void createMember(MemberRequest memberRequest) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME_MEMBER, memberRequest.getId());
        if(o != null) {
            throw new DuplicateResourceException("해당 ID에 대해 Member가 이미 존재합니다.");
        }

        String password = passwordEncoder.encode(memberRequest.getPassword());
        Member member = new Member(memberRequest.getId(), memberRequest.getName(), password, memberRequest.getAge(), memberRequest.getRole());
        redisTemplate.opsForHash().put(HASH_NAME_MEMBER, memberRequest.getId(), member);
    }

    public Member getMember(String memberId) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME_MEMBER, memberId);
        if(o == null) {
            throw new NotFoundMemberException("해당 ID 값에 대한 Member가 존재하지 않습니다.");
        }

        return objectMapper.convertValue(o, Member.class);
    }

    public List<Member> getMembers() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME_MEMBER);

        List<Member> members = new ArrayList<>(entries.size());
        for(Object value : entries.values()) {
            Member member = objectMapper.convertValue(value, Member.class);
            members.add(member);
        }

        return members;
    }

}
