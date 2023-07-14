package com.project.taskmanager.task.domain;

import com.project.taskmanager.task.domain.Attachment;
import com.project.taskmanager.task.domain.Comment;
import com.project.taskmanager.task.domain.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "tasks")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Title cannot be empty")
    private String title;
    @NotNull(message = "Description cannot be empty")
    private String description;
    @Builder.Default
    private TaskStatus status = TaskStatus.OPEN;
    private UUID creatorId;
    @Builder.Default
    private UUID assigneeId = null;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    private UUID updatedById;

    //Relationships

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();
}
