package com.daily.demo.service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;
import com.daily.demo.repository.user.UserCustomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private final UserCustomRepository userCustomRepository;

    public boolean sendMail(String targetEmail) {
        boolean result = false;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        UserResponse userResponse = userCustomRepository.findEmail(targetEmail, Useyn.Y)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

        simpleMailMessage.setTo("jet2303@naver.com");
        simpleMailMessage.setFrom(null);
        simpleMailMessage.setSubject("send user info");
        simpleMailMessage.setText(userResponse.getPassword());

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result = true;
    }

}
