package com.project.taskmanager.task.context.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String creatorId;
    private String creatorName;
    private String createdAt;
    private String updatedAt;
    private Long taskId;
}
