package com.hoanghai.todo.services;

import com.hoanghai.todo.entities.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface TaskDependencyService {
    @Transactional
    void addDependency(Long taskId, Long dependencyId);

    @Transactional
    void removeDependency(Long taskId, Long dependencyId);

    List<Task> getDirectDependencies(Long taskId);

    List<Task> getAllDependencies(Long taskId);

    boolean hasCircularDependency(Task startTask, Task endTask);

    boolean checkDependency(Task currentTask, Task targetTask, Set<Task> visited);
}
