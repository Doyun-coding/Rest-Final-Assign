package com.nhnacademy.restfinal.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthGoogleController {

    @GetMapping("/google")
    public String getAuthGoogle() {
        return "auth/google";
    }

}
