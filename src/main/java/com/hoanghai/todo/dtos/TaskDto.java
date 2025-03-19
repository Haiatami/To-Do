package com.hoanghai.todo.dtos;

import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskStatus status;

    private TaskPriority priority;
}
