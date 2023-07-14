package com.project.taskmanager.task.context.task.dto;

import com.project.taskmanager.task.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TaskUpdateRequest {
    private String title;
    private String description;
    private UUID assigneeId;
    private TaskStatus status;
}
