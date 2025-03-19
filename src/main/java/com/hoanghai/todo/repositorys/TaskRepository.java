package com.hoanghai.todo.repositorys;

import com.hoanghai.todo.entities.Task;
import com.hoanghai.todo.enums.TaskPriority;
import com.hoanghai.todo.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:dueDate IS NULL OR t.dueDate >= :dueDate) AND " +
            "(:statuses IS NULL OR t.status IN :statuses) AND " +
            "(:priorities IS NULL OR t.priority IN :priorities)")
    Page<Task> findAll( // Using @Query for your custom findAll
                        @Param("title") String title,
                        @Param("description") String description,
                        @Param("dueDate") LocalDate dueDate,
                        @Param("statuses") List<TaskStatus> statuses,
                        @Param("priorities") List<TaskPriority> priorities,
                        Pageable pageable);

    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByDueDateBeforeAndStatusNot(LocalDate dueDate, TaskStatus status);
}