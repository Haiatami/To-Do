package com.hoanghai.todo.controllers;

import com.hoanghai.todo.dtos.CreateTaskRequest;
import com.hoanghai.todo.dtos.TaskDto;
import com.hoanghai.todo.dtos.UpdateTaskRequest;
import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import com.hoanghai.todo.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        Task task = taskService.createTask(createTaskRequest);// tạo các task
        return new ResponseEntity<>(convertToDto(task), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(required = false) List<TaskStatus> statuses,
            @RequestParam(required = false) List<TaskPriority> priorities,
            Pageable pageable) {
        Page<Task> tasks = taskService.getAllTasks(title, description, dueDate, statuses, priorities, pageable);// lấy tất cả các task và dựa vào tiêu đề, thời hạn, trạng thái, độ quan trọng và trang
        return ResponseEntity.ok(tasks.map(this::convertToDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id) // lấy thông tin task dựa vào id
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(id, updateTaskRequest) // thay đổi thông tin và trạng thái của task
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id); // thực hiện xóa task
        return ResponseEntity.noContent().build();
    }

    private TaskDto convertToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();
    }
}
