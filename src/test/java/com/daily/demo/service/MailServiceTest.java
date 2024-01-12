package com.daily.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daily.demo.service.mail.MailRequest;
import com.daily.demo.service.mail.MailService;

@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    void 메일전송() {

        // MailRequest mailRequest = MailRequest.builder()
        // .toMail("jet2303@naver.com")
        // .fromMail("test@naver.com")
        // .mailSubject("test mail subject")
        // .mailBody("test mail body")
        // .build();

        mailService.sendMail("admin@admin.com");
    }
}
