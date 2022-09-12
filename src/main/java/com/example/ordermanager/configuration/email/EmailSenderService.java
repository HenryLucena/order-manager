package com.example.ordermanager.configuration.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class EmailSenderService {
    private static final Logger logCommon = LogManager.getLogger("managerLogger");

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("envioteste00@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject("Order manager");

        logCommon.info(format("Email has been send to: %s", toEmail));
        javaMailSender.send(message);
    }
}

