package com.project.taskmanager.task.context.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String creatorId;
    private String creatorName;
    private String assigneeId;
    private String assigneeName;
    private String createdAt;
    private String updatedAt;
    private String updatedById;
    private String updatedByName;
}
