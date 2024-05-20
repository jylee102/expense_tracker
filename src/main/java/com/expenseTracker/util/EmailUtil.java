package com.expenseTracker.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public void sendVerificationCode(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lilydodo123@naver.com");
        message.setTo(toEmail);
        message.setSubject("이메일 인증 코드");
        message.setText("임시 비밀번호는 " + verificationCode + " 입니다.\n" +
                "로그인 후 [설정/관리 > 개인 정보 수정]에서 비밀번호를 변경하시길 바랍니다.");

        javaMailSender.send(message);
    }
}
