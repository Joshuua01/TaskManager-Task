package com.project.taskmanager.task.context.task.dto;

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
public class TaskRequest {
    @NotBlank(message = "Title can not be empty")
    private String title;
    @NotBlank(message = "Description can not be empty")
    private String description;
    private UUID assigneeId;
}
