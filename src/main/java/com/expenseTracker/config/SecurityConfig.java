package com.expenseTracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // spring security filterChain이 자동으로 포함되는 클래스를 만들어준다.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 1. 페이지 접근에 관한 설정(인증, 인가 - 로그인 이후에)
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                // 모든 사용자가 로그인(인증)없이 접근할 수 있도록 설정
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/fonts/**", "/scss/**", "/vendors/**").permitAll()
                .requestMatchers( "/members/**").permitAll()
                .requestMatchers("/favicon.ico", "/error").permitAll()

                // 그 외의 페이지는 모두 로그인(인증)을 해야 한다.
                .anyRequest().authenticated() // CustomAuthenticationEntryPoint에 있는 commence() 메소드를 실행

        // 2. 로그인에 관한 설정
        ).formLogin(formLogin -> formLogin
                .loginPage("/members/login") // 로그인 페이지 URL
                .defaultSuccessUrl("/") // 로그인 성공시 이동할 페이지 URL
                .usernameParameter("email") // ★ 로그인시 id로 사용할 파라메터 이름(내 사이트에 맞는 걸로 바꿔줘야 한다.)
                .failureUrl("/members/login/error") // 로그인 실패시 이동할 페이지 URL

        // 3. 로그아웃에 관한 설정
        ).logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃시 이동할 URL
                .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 URL
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제

        // 4. 인증되지 않은 사용자가 리소스에 접근시 설정
        ).exceptionHandling(handling -> handling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

        ).sessionManagement(sessionManagement -> sessionManagement
                .sessionFixation().migrateSession() // 세션 고정 보호 정책
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 정책
                .invalidSessionUrl("/members/login") // 만료된 세션으로 요청이 온 경우 이동할 URL
                .maximumSessions(1).expiredUrl("/members/login?expired=true") // 동시 세션 제어 및 만료된 세션 처리

        ).rememberMe(Customizer.withDefaults()); // 로그인 이후 세션을 통해 로그인 유지

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
