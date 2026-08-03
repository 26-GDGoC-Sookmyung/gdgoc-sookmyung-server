package com.gdgoc.website.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dscsookmyung@gmail.com");
        message.setTo(toEmail);
        message.setSubject("[GDGoC Sookmyung] 이메일 인증번호 안내");
        message.setText("안녕하세요, GDGoC Sookmyung 웹사이트입니다.\n\n"
                + "회원가입 인증번호는 [" + code + "] 입니다.\n"
                + "3분 이내에 홈페이지에 입력해주세요.");
        
        javaMailSender.send(message);
    }
}