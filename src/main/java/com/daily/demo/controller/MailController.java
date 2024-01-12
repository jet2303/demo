package com.daily.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daily.demo.service.mail.MailRequest;
import com.daily.demo.service.mail.MailService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/find_email")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/")
    public void getMethodName(@RequestBody String targetEmail) {
        mailService.sendMail(targetEmail);
    }

}
