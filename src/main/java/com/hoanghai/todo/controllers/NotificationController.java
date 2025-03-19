package com.hoanghai.todo.controllers;

import com.hoanghai.todo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/trigger-task-check")
    public ResponseEntity<String> triggerTaskNotificationCheck() {
        notificationService.checkUpcomingAndOverdueTasks();
        return new ResponseEntity<>("Task notification check triggered successfully.", HttpStatus.OK);
    }
}
