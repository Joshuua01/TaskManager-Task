package com.project.taskmanager.task.infrastructure;

import com.project.taskmanager.task.context.comment.CommentService;
import com.project.taskmanager.task.context.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserUtility {
    private final TaskService taskService;
    private final CommentService commentService;


    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    public boolean isCommentCreator(Long commentId) {
        return commentService.getCommentById(commentId).getCreatorId().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public boolean isTaskCreator(Long taskId) {
        return taskService.getTaskById(taskId).getCreatorId().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
