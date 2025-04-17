package com.nhnacademy.restfinalassign.controller;

import com.nhnacademy.restfinalassign.model.domain.Member;
import com.nhnacademy.restfinalassign.model.request.MemberRequest;
import com.nhnacademy.restfinalassign.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/{memberId}")
    public Member getMember(@PathVariable String memberId) {
        return memberService.getMember(memberId);
    }

    @GetMapping("/member")
    public List<Member> getMembers() {
        return memberService.getMembers();
    }


}
