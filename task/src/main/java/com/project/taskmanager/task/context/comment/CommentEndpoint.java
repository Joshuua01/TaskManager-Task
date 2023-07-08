package com.project.taskmanager.task.context.comment;

import com.project.taskmanager.task.context.comment.dto.CommentRequest;
import com.project.taskmanager.task.context.comment.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentEndpoint {
    private final CommentService commentService;

    @PostMapping("/create/{taskId}")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long taskId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(taskId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/all/{taskId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getAllCommentsByTaskId(taskId));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

}
