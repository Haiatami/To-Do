package com.hoanghai.todo.services;

import com.hoanghai.todo.dtos.CreateTaskRequest;
import com.hoanghai.todo.dtos.UpdateTaskRequest;
import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(CreateTaskRequest request);

    Page<Task> getAllTasks(String title, String description, LocalDate dueDate, List<TaskStatus> statuses, List<TaskPriority> priorities, Pageable pageable);

    Optional<Task> getTaskById(Long id);

    Optional<Task> updateTask(Long id, UpdateTaskRequest request);

    @Transactional
    void deleteTask(Long id);
}
