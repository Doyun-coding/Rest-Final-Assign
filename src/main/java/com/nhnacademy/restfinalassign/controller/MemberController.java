package com.nhnacademy.restfinalassign.controller;

import com.nhnacademy.restfinalassign.model.request.MemberRequest;
import com.nhnacademy.restfinalassign.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);

        return ResponseEntity.ok().build();
    }


}
