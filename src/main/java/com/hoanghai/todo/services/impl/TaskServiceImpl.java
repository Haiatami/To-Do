package com.hoanghai.todo.services.impl;

import com.hoanghai.todo.dtos.CreateTaskRequest;
import com.hoanghai.todo.dtos.UpdateTaskRequest;
import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import com.hoanghai.todo.repositorys.TaskRepository;
import com.hoanghai.todo.services.NotificationService;
import com.hoanghai.todo.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Task createTask(CreateTaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .priority(request.getPriority())
                .status(TaskStatus.TODO) // Default status
                .build();
        Task savedTask = taskRepository.save(task);
        // Send notification when a task is created with a future due date
        if (savedTask.getDueDate() != null && savedTask.getDueDate().isAfter(LocalDate.now())) {
            // Assuming you have a way to get the user's email associated with the task
            String userEmail = "user@example.com"; // Replace with actual user email retrieval
            notificationService.sendTaskCreatedNotification(savedTask, userEmail);
        }
        return savedTask;
    }

    @Override
    public Page<Task> getAllTasks(String title, String description, LocalDate dueDate, List<TaskStatus> statuses, List<TaskPriority> priorities, Pageable pageable) {
        return taskRepository.findAll(title, description, dueDate, statuses, priorities, pageable);
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    @CachePut(value = "tasks", key = "#id")
    public Optional<Task> updateTask(Long id, UpdateTaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    LocalDate originalDueDate = task.getDueDate(); // Get the original due date

                    if (request.getTitle() != null) {
                        task.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        task.setDescription(request.getDescription());
                    }
                    if (request.getDueDate() != null) {
                        task.setDueDate(request.getDueDate());
                    }
                    if (request.getStatus() != null) {
                        task.setStatus(request.getStatus());
                    }
                    if (request.getPriority() != null) {
                        task.setPriority(request.getPriority());
                    }
                    Task updatedTask = taskRepository.save(task);

                    // Send notification if the due date is updated
                    if (request.getDueDate() != null && !request.getDueDate().equals(originalDueDate)) {
                        // Assuming you have a way to get the user's email
                        String userEmail = "user@example.com"; // Replace with actual user email retrieval
                        notificationService.sendTaskUpdatedNotification(updatedTask, userEmail, originalDueDate);
                    }
                    return updatedTask;
                });
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
        // You might want to send a notification when a task is deleted as well
    }
}

