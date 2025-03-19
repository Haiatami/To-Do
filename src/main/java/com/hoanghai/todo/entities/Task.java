package com.hoanghai.todo.entities;

import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter // Add this annotation
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")
public class Task implements Serializable { // Triển khai giao diện Serializable

    private static final long serialVersionUID = 1L; // Được đề xuất cho phiên bản

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được bỏ trống")
    private String title;

    private String description;

    @NotNull(message = "Thời hạn không được bỏ trống")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO; // TODO, IN_PROGRESS, COMPLETED, BLOCKED

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Mức độ quan trọng không được bỏ trống")
    private TaskPriority priority = TaskPriority.MEDIUM; // LOW, MEDIUM, HIGH

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}