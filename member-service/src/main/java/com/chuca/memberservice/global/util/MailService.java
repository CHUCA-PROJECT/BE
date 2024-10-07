package com.chuca.memberservice.global.util;

import com.chuca.memberservice.domain.member.domain.repository.MemberRepository;
import com.chuca.memberservice.global.exception.BaseException;
import com.chuca.memberservice.global.exception.EmailException;
import com.chuca.memberservice.global.response.ErrorCode;
import com.chuca.memberservice.global.response.ErrorResponse;
import com.chuca.memberservice.global.security.JwtProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    // 인증 코드 이메일 내용
    private MimeMessage createCodeMessage(String code, String emailTo) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailTo); // 보내는 사람
        message.setSubject("\uD83C\uDF89 CHUCA 이메일 인증번호 \uD83C\uDF89"); // 메일 제목
        message.setText(code, "utf-8", "html"); // 내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"CHUCA_Official")); // 보내는 사람의 메일 주소, 보내는 사람 이름

        log.info("message : " + message);
        return message;
    }

    // 인증 코드 생성 (5자리로 생성)
    private String createCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();

        for(int i=0; i<5; i++) {
            code.append((random.nextInt(10)));
        }

        return code.toString();
    }

    // 메일로 인증번호 발송
    public String sendCodeMail(String email) {
        String code = createCode();
        try{
            MimeMessage mimeMessage = createCodeMessage(code, email);
            javaMailSender.send(mimeMessage);
            return code;
        } catch (Exception e){
            e.printStackTrace();
            throw new EmailException(ErrorCode.EMAIL_SEND_ERROR);
        }
    }
}
