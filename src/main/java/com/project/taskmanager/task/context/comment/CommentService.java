package com.project.taskmanager.task.context.comment;

import com.project.taskmanager.task.context.comment.dto.CommentRequest;
import com.project.taskmanager.task.context.comment.dto.CommentResponse;
import com.project.taskmanager.task.domain.Comment;
import com.project.taskmanager.task.infrastructure.CommentRepository;
import com.project.taskmanager.task.infrastructure.TaskRepository;
import com.project.taskmanager.task.infrastructure.UserUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserUtility userUtility;


    public CommentResponse createComment(Long taskId, CommentRequest request) {
        var comment = Comment.builder()
                .content(request.getContent())
                .task(taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found")))
                .creatorId(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();
        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .creatorId(comment.getCreatorId().toString())
                .creatorName(userUtility.getUserWebClient(comment.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(comment.getCreatedAt().toString())
                .updatedAt(comment.getUpdatedAt().toString())
                .taskId(comment.getTask().getId())
                .build();
    }

    public CommentResponse getCommentById(Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .creatorId(comment.getCreatorId().toString())
                .createdAt(comment.getCreatedAt().toString())
                .creatorName(userUtility.getUserWebClient(comment.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .updatedAt(comment.getUpdatedAt().toString())
                .taskId(comment.getTask().getId())
                .build();
    }

    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .creatorId(comment.getCreatorId().toString())
                    .creatorName(userUtility.getUserWebClient(comment.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                    .createdAt(comment.getCreatedAt().toString())
                    .updatedAt(comment.getUpdatedAt().toString())
                    .taskId(comment.getTask().getId())
                    .build();
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }


    public List<CommentResponse> getAllCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findAllByTaskId(taskId);

        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .creatorId(comment.getCreatorId().toString())
                    .creatorName(userUtility.getUserWebClient(comment.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                    .createdAt(comment.getCreatedAt().toString())
                    .updatedAt(comment.getUpdatedAt().toString())
                    .taskId(comment.getTask().getId())
                    .build();
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    public CommentResponse updateComment(Long id, CommentRequest request) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(request.getContent() != null ? request.getContent() : comment.getContent());
        comment.setUpdatedAt(comment.getUpdatedAt());
        commentRepository.save(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .creatorId(comment.getCreatorId().toString())
                .creatorName(userUtility.getUserWebClient(comment.getCreatorId()).get().retrieve().bodyToMono(String.class).block())
                .createdAt(comment.getCreatedAt().toString())
                .updatedAt(comment.getUpdatedAt().toString())
                .taskId(comment.getTask().getId())
                .build();
    }

    public void deleteComment(Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
