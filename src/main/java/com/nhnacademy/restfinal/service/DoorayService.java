package com.nhnacademy.restfinal.service;

import com.nhnacademy.restfinal.model.DoorayFeign;
import com.nhnacademy.restfinal.model.request.DoorayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoorayService {

    private final DoorayFeign doorayFeign;

    public void sendMessage(String username) {
        doorayFeign.sendMessage(new DoorayRequest("차단 알림",  username + " 님 로그인 기능이 1분간 차단됐습니다."), "3204376758577275363", "4045901689874472590", "W0RgKMoPTUO3RejIIJVQcg");
    }

}
