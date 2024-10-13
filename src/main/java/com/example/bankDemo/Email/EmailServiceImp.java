package com.example.bankDemo.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService{

    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    @Autowired
    public EmailServiceImp(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    @Override
    public void sendMailAlert(EmailDetails emailDetails) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(senderEmail);
                mailMessage.setTo(emailDetails.getRecipient());
                mailMessage.setText(emailDetails.getMessageBody());
                mailMessage.setSubject(emailDetails.getSubject());

                javaMailSender.send(mailMessage);
                System.out.println("Email sent");
            }
            catch (Exception e){
                System.out.println("Email not sent");
                throw new RuntimeException("Failed to send email", e);
            }
    }
}
