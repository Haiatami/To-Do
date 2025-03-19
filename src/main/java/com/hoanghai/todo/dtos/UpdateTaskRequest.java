package com.hoanghai.todo.dtos;

import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskRequest {

    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    private TaskPriority priority;
}
