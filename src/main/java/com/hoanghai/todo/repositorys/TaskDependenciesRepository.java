package com.hoanghai.todo.repositorys;

import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.entities.TaskDependencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskDependenciesRepository extends JpaRepository<TaskDependencies, Long> {

    List<TaskDependencies> findByTask(Task task);

    Optional<TaskDependencies> findByTaskAndDependency(Task task, Task dependency);

    void deleteByTaskAndDependency(Task task, Task dependency);

    List<TaskDependencies> findByDependency(Task dependency);
}