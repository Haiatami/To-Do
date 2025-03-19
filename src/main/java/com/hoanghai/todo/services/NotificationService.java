package com.hoanghai.todo.services;

import com.hoanghai.todo.entities.Task;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

public interface NotificationService {
    // Run every day at a specific time (e.g., 8:00 AM)
    @Scheduled(cron = "0 0 8 * * *")
    void checkUpcomingAndOverdueTasks();

    void sendTaskCreatedNotification(Task task, String userEmail);

    void sendTaskUpdatedNotification(Task task, String userEmail, LocalDate originalDueDate);
}
