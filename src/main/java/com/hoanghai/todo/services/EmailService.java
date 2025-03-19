package com.hoanghai.todo.services;

public interface EmailService {
    void sendNotificationEmail(String to, String subject, String body);
}
