package com.project.taskmanager.task.context.task.dto;

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
    private String title;
    private String description;
    private UUID assigneeId;
}
