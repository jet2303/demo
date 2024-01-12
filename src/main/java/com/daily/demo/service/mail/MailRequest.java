package com.daily.demo.service.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class MailRequest {

    private String toMail;
    private String fromMail;
    private String mailSubject;
    private String mailBody;

}
