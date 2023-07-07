package com.project.taskmanager.task.domain;

import com.project.taskmanager.task.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "tasks")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private UUID CreatorId;
    private UUID AssigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
