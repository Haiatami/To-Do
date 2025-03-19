package com.hoanghai.todo.controllers;

import com.hoanghai.todo.dtos.TaskDto;
import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.services.TaskDependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks/{taskId}/dependencies")
@RequiredArgsConstructor
public class TaskDependencyController {
    @Autowired
    private TaskDependencyService taskDependencyService;

    @PostMapping("/{dependencyId}")
    public ResponseEntity<String> addDependency(@PathVariable Long taskId, @PathVariable Long dependencyId) {
        try {
            taskDependencyService.addDependency(taskId, dependencyId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Dependency added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{dependencyId}")
    public ResponseEntity<String> removeDependency(@PathVariable Long taskId, @PathVariable Long dependencyId) {
        taskDependencyService.removeDependency(taskId, dependencyId);
        return ResponseEntity.ok("Dependency removed successfully.");
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getDependencies(@PathVariable Long taskId) {
        List<Task> dependencies = taskDependencyService.getDirectDependencies(taskId);
        return ResponseEntity.ok(dependencies.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllDependencies(@PathVariable Long taskId) {
        List<Task> allDependencies = taskDependencyService.getAllDependencies(taskId);
        return ResponseEntity.ok(allDependencies.stream().map(this::convertToDto).collect(Collectors.toList()));
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