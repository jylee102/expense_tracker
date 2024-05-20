package com.expenseTracker.controller;

import com.expenseTracker.dto.MemberFormDto;
import com.expenseTracker.entity.Member;
import com.expenseTracker.service.MemberService;
import com.expenseTracker.util.EmailUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final EmailUtil emailUtil;


    // 로그인 화면
    @GetMapping(value = "/members/login") // localhost/members/login
    public String loginMember() {
        return "member/memberLoginForm"; // memberLoginForm.html
    }

    // 회원가입 화면
    @GetMapping("/members/new") // localhost/members/new
    public String memberForm(Model model) {
        // 유효성 체크를 위해서 memberFormDto 객체를 매핑하기 위해 전달
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm"; // memberForm.html
    }

    // 회원가입 처리
    @PostMapping(value = "/members/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        // 유효성 검증 에러 발생시 회원가입페이지로 이동시킴
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
                sb.append("\n");
            }

            model.addAttribute("errorMessage", sb.toString());
            return "member/memberForm";
        }

        // 유효성 검사를 통과했다면 회원가입 진행
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            // 회원가입이 이미 되어있다면
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/"; // 회원가입 완료 후 메인페이지로 이동
    }

    // 로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm"; // 로그인 페이지로 그대로 이동
    }

    // 개인정보 수정
    @PostMapping(value = "/members/updateMyInfo")
    public String updateMyInfo(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            // 유효성 체크 후 에러 결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러 메시지를 가지고 온다.

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
                sb.append("\n");
            }

            model.addAttribute("message", sb.toString());
            return "/settings/myInfo";
        }

        try {
            memberService.updateMember(memberFormDto);
            model.addAttribute("message", "회원 정보가 성공적으로 변경되었습니다.");
            return "/settings/myInfo";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "회원 정보를 수정하는 도중 에러가 발생했습니다.");
            return "redirect:/settings/myInfo";
        }
    }

    // 비밀번호 변경
    @PostMapping("/members/changePw")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {

        Member member = memberService.getMember(principal.getName());
        model.addAttribute("memberFormDto", MemberFormDto.of(member));

        // 현재 비밀번호 확인
        if (!memberService.checkPassword(member, currentPassword)) {
            model.addAttribute("message", "현재 비밀번호가 틀립니다.");
            return "/settings/myInfo";
        }

        if (newPassword.length() < 8 || newPassword.length() > 16) {
            model.addAttribute("message", "비밀번호는 8자 ~ 16자 사이로 입력해주세요.");
            return "/settings/myInfo";
        }

        // 이전에 사용된 비밀번호와 새로운 비밀번호가 다른지 확인
        if (memberService.checkPassword(member, newPassword)) {
            model.addAttribute("message", "새 비밀번호는 이전에 사용한 비밀번호와 달라야 합니다.");
            return "/settings/myInfo";
        }

        // 새로운 비밀번호와 확인 비밀번호 일치 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "/settings/myInfo";
        }

        // 비밀번호 업데이트
        memberService.updatePassword(member, newPassword);

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");

        return "/settings/myInfo";
    }

    // 임시 비밀번호 보내기
    @PostMapping(value = "/members/sendNewPassword")
    public @ResponseBody ResponseEntity sendNewPassword(@RequestBody Map<String, String> req) {
        try {
            Member member = memberService.getMember(req.get("email"));

            if (member == null) {
                return new ResponseEntity("해당 이메일로 된 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }

            String password = generateRandomCode();
            emailUtil.sendVerificationCode(member.getEmail(), password); // 이메일로 임시 비밀번호 전송

            // 비밀번호 임시 비밀번호로 업데이트
            memberService.updatePassword(member, password);
            return new ResponseEntity("해당 이메일로 임시 비밀번호가 전송되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("현재 비밀번호 초기화를 이용할 수 없습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    // 임시 비밀번호 생성
    public String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
}
