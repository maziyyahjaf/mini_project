package com.example.maziyyah.light_touch.light_touch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmailId;

    public void sendPartnerJoinedEmail(String toEmail, String partnerName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailId);
        message.setTo(toEmail);
        message.setSubject("Your partner has joined");
        message.setText("Hi! Your partner " + partnerName + "has just signed up and linked with you.");
        javaMailSender.send(message);

    }
}
