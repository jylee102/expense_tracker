package com.expenseTracker.controller;

import com.expenseTracker.dto.MemberFormDto;
import com.expenseTracker.entity.Member;
import com.expenseTracker.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc // mockMvc 테스트를 위해 어노테이션 선언
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc; // 모형 객체 => 웹 브라우저에서 request 하는 것처럼 테스트 할 수 있는 객체

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setPhone("010-0000-0000");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("사용자 계정 생성 테스트")
    public void signUpTest() throws Exception {
        String email = "test@gmail.com";
        String password = "1234";
        Member savedMember = createMember(email, password);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@gmail.com";
        String password = "1234";
        createMember(email, password);

        mockMvc.perform(SecurityMockMvcRequestBuilders
                .formLogin().userParameter("email")
                .loginProcessingUrl("/members/login") // 로그인 처리할 경로
                .user(email).password(password)
        ).andExpect(SecurityMockMvcResultMatchers.authenticated()); // 로그인이 성공하면 테스트 코드를 통과시킴
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@gmail.com";
        String password = "1234";
        createMember(email, password);

        mockMvc.perform(SecurityMockMvcRequestBuilders
                .formLogin().userParameter("email")
                .loginProcessingUrl("/members/login") // 로그인 처리할 경로
                .user(email).password("12345") //비밀번호 일부러 다르게 입력
        ).andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // 로그인이 실패하면 테스트 코드를 통과시킴
    }
}