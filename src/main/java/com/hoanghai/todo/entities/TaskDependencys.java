package com.hoanghai.todo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "task_dependency")
public class TaskDependencys {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @ManyToOne
    @JoinColumn(name = "depends_task_id")
    private Tasks dependsTask;
}
