package com.nhnacademy.restfinal.common.config;

import com.nhnacademy.restfinal.common.filter.JwtFilter;
import com.nhnacademy.restfinal.common.filter.RedisSessionFilter;
import com.nhnacademy.restfinal.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.restfinal.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.restfinal.handler.CustomGoogleSuccessHandler;
import com.nhnacademy.restfinal.handler.CustomLogoutHandler;
import com.nhnacademy.restfinal.service.CustomOAuth2UserService;
import com.nhnacademy.restfinal.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     *
     * login 과정 : SecurityConfig .formLogin -> CustomUserDetailsService(UserDetailsService) -> AuthMember(UserDetails)
     * -> CustomAuthenticationSuccessHandler -> LoginAttemptService -> CustomAuthenticationSuccessHandler -> RedisSessionFilter
     *
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService, RedisSessionFilter redisSessionFilter) throws Exception {
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler = new CustomAuthenticationSuccessHandler(loginAttemptService, redisTemplate);
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler = new CustomAuthenticationFailureHandler(loginAttemptService);
        CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(redisTemplate);

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth // 권한에 따른 접근 가능 페이지
                        .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth/member/**").hasRole("MEMBER")
                        .requestMatchers("/auth/google/**").hasRole("GOOGLE")
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form // 로그인
                        .loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("pwd")
                        .loginProcessingUrl("/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2 // Google 로그인
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/auth/google")) // 로그인 성공 후 리다이렉션 경로

                .logout(logout -> logout // 로그아웃
                        .addLogoutHandler(customLogoutHandler)
                        .logoutUrl("/logout"))
                .addFilterBefore(redisSessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();  // 평문 그대로 비교
//    }

}
