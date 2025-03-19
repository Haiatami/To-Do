package com.hoanghai.todo.services.impl;

import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.enums.TaskStatus;
import com.hoanghai.todo.repositorys.TaskRepository;
import com.hoanghai.todo.services.EmailService;
import com.hoanghai.todo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    // Run every day at a specific time (e.g., 8:00 AM)
    @Scheduled(cron = "0 0 8 * * *")
    @Override
    public void checkUpcomingAndOverdueTasks() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Task> upcomingTasks = taskRepository.findByDueDate(tomorrow);
        upcomingTasks.forEach(task -> {
            String subject = "Reminder: Task Due Tomorrow";
            String body = String.format("Hi,<br><br>This is a reminder that the task <b>%s</b> is due tomorrow, %s.<br><br>Best regards,<br>Your To-Do App",
                    task.getTitle(), task.getDueDate());
            String userEmail = "user@example.com"; // Replace with actual user email retrieval
            emailService.sendNotificationEmail(userEmail, subject, body);
            logger.info("Upcoming task notification sent for: {}", task.getTitle());
        });

        List<Task> overdueTasks = taskRepository.findByDueDateBeforeAndStatusNot(today, TaskStatus.COMPLETED);
        overdueTasks.forEach(task -> {
            String subject = "Overdue Task";
            String body = String.format("Hi,<br><br>The task <b>%s</b> was due on %s and is now overdue. Please take action.<br><br>Best regards,<br>Your To-Do App",
                    task.getTitle(), task.getDueDate());
            String userEmail = "user@example.com"; // Replace with actual user email retrieval
            emailService.sendNotificationEmail(userEmail, subject, body);
            logger.warn("Overdue task notification sent for: {}", task.getTitle());
        });
    }

    @Override
    public void sendTaskCreatedNotification(Task task, String userEmail) {
        String subject = "New Task Created";
        String body = String.format("Hi,<br><br>A new task has been created:<br><b>Title:</b> %s<br><b>Due Date:</b> %s<br><b>Description:</b> %s<br><br>Best regards,<br>Your To-Do App",
                task.getTitle(), task.getDueDate(), task.getDescription());
        emailService.sendNotificationEmail(userEmail, subject, body);
        logger.info("Task created notification sent for task: {}", task.getTitle());
    }

    @Override
    public void sendTaskUpdatedNotification(Task task, String userEmail, LocalDate originalDueDate) {
        String subject = "Task Updated";
        StringBuilder bodyBuilder = new StringBuilder("Hi,<br><br>The following task has been updated:<br>");
        bodyBuilder.append(String.format("<b>Title:</b> %s<br>", task.getTitle()));
        if (!task.getDueDate().equals(originalDueDate)) {
            bodyBuilder.append(String.format("<b>Due Date:</b> Updated from %s to %s<br>", originalDueDate, task.getDueDate()));
        }
        bodyBuilder.append(String.format("<b>Description:</b> %s<br>", task.getDescription()));
        bodyBuilder.append(String.format("<b>Status:</b> %s<br>", task.getStatus()));
        bodyBuilder.append(String.format("<b>Priority:</b> %s<br><br>", task.getPriority()));
        bodyBuilder.append("Best regards,<br>Your To-Do App");

        emailService.sendNotificationEmail(userEmail, subject, bodyBuilder.toString());
        logger.info("Task updated notification sent for task: {}", task.getTitle());
    }
}
