package com.hoanghai.todo.services.impl;

import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.entities.TaskDependencies;
import com.hoanghai.todo.repositorys.TaskDependenciesRepository;
import com.hoanghai.todo.repositorys.TaskRepository;
import com.hoanghai.todo.services.TaskDependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskDependencyServiceImpl implements TaskDependencyService {
    @Autowired
    private TaskDependenciesRepository taskDependenciesRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    @Override
    public void addDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        Task dependency = taskRepository.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependency task not found with id: " + dependencyId));

        if (task.equals(dependency)) {
            throw new IllegalArgumentException("A task cannot depend on itself.");
        }

        if (taskDependenciesRepository.findByTaskAndDependency(task, dependency).isPresent()) {
            return; // Dependency already exists
        }

        // Circular dependency check
        if (hasCircularDependency(task, dependency)) {
            throw new IllegalArgumentException("Creating this dependency would introduce a circular dependency.");
        }

        TaskDependencies taskDependencies = TaskDependencies.builder()
                .task(task)
                .dependency(dependency)
                .build();
        taskDependenciesRepository.save(taskDependencies);
    }

    @Transactional
    @Override
    public void removeDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        Task dependency = taskRepository.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependency task not found with id: " + dependencyId));

        taskDependenciesRepository.deleteByTaskAndDependency(task, dependency);
    }

    @Override
    public List<Task> getDirectDependencies(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return taskDependenciesRepository.findByTask(task).stream()
                .map(TaskDependencies::getDependency)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllDependencies(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        Set<Task> allDependencies = new HashSet<>();
        Set<Task> visited = new HashSet<>();
        Queue<Task> queue = new LinkedList<>();

        queue.offer(task);
        visited.add(task);

        while (!queue.isEmpty()) {
            Task currentTask = queue.poll();
            List<Task> directDependencies = getDirectDependencies(currentTask.getId());
            for (Task dependency : directDependencies) {
                if (!visited.contains(dependency)) {
                    allDependencies.add(dependency);
                    visited.add(dependency);
                    queue.offer(dependency);
                }
            }
        }
        return new ArrayList<>(allDependencies);
    }

    @Override
    public boolean hasCircularDependency(Task startTask, Task endTask) {
        Set<Task> visited = new HashSet<>();
        return checkDependency(startTask, endTask, visited);
    }

    @Override
    public boolean checkDependency(Task currentTask, Task targetTask, Set<Task> visited) {
        visited.add(currentTask);
        List<Task> dependencies = getDirectDependencies(currentTask.getId());
        for (Task dependency : dependencies) {
            if (dependency.equals(targetTask)) {
                return true;
            }
            if (!visited.contains(dependency)) {
                if (checkDependency(dependency, targetTask, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
}

