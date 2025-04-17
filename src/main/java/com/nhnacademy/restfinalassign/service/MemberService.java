package com.nhnacademy.restfinalassign.service;

import com.nhnacademy.restfinalassign.exception.DuplicateResourceException;
import com.nhnacademy.restfinalassign.model.domain.Member;
import com.nhnacademy.restfinalassign.model.request.MemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HASH_NAME_MEMBER = "Member:";

    public void createMember(MemberRequest memberRequest) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME_MEMBER, memberRequest.getId());
        if(o != null) {
            throw new DuplicateResourceException("해당 ID에 대해 Member가 이미 존재합니다.");
        }

        Member member = new Member(memberRequest.getId(), memberRequest.getName(), memberRequest.getPassword(), memberRequest.getAge(), memberRequest.getRole());
        redisTemplate.opsForHash().put(HASH_NAME_MEMBER, memberRequest.getId(), member);
    }

}
