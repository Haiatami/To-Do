package com.hoanghai.todo.services.impl;

import com.hoanghai.todo.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendNotificationEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart for HTML content

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true for HTML

            mailSender.send(message);
            logger.info("Notification email sent to: {}", to);

        } catch (MessagingException e) {
            logger.error("Error sending notification email to {}: {}", to, e.getMessage());
        }
    }
}
