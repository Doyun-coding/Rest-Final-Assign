package com.nhnacademy.restfinal.common.config;

import com.nhnacademy.restfinal.service.CustomOAuth2UserService;
import com.nhnacademy.restfinal.service.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Security Config Member Login 200 Test")
    @WithMockUser(username = "1", roles = {"MEMBER"})
    void securityConfigMemberLogin200Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/member"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Security Config Member Login 403 Test")
    @WithMockUser(username = "1")
    void securityConfigMemberLogin403Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/member"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Security Config Admin Login 200 Test")
    @WithMockUser(username = "1", roles = {"ADMIN"})
    void securityConfigAdminLogin200Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Security Config Admin Login 403 Test")
    @WithMockUser(username = "1")
    void securityConfigAdminLogin403Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Security Config Google Login 200 Test")
    @WithMockUser(username = "1", roles = {"GOOGLE"})
    void securityConfigGoogleLogin200Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/google"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Security Config Google Login 403 Test")
    @WithMockUser(username = "1")
    void securityConfigGoogleLogin403Test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/google"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}