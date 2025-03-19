package com.hoanghai.todo.dtos;

import com.hoanghai.todo.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Tiêu đề không được bỏ trống")
    private String title;

    private String description;

    @NotNull(message = "Thời hạn không được bỏ trống")
    private LocalDate dueDate;

    @NotNull(message = "Mức độ quan trọng không được bỏ trống")
    private TaskPriority priority;
}