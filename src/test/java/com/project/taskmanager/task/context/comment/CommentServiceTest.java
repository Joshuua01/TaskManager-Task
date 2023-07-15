package com.project.taskmanager.task.context.comment;

import com.project.taskmanager.task.context.comment.dto.CommentRequest;
import com.project.taskmanager.task.domain.Comment;
import com.project.taskmanager.task.infrastructure.CommentRepository;
import com.project.taskmanager.task.infrastructure.TaskRepository;
import com.project.taskmanager.task.infrastructure.UserUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserUtility userUtility;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, taskRepository, userUtility);
    }

    @Test
    void testCreateCommentWithInvalidTaskExpectExceptionThrown(){
        Long taskId = 1L;
        CommentRequest request = new CommentRequest();
        request.setContent("Test Comment");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(taskId, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task not found");

        verify(taskRepository, times(1)).findById(taskId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetCommentByIdWithNonExistingCommentExpectExceptionThrown() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getCommentById(commentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Comment not found");

        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testUpdateCommentWithNonExistingCommentExpectExceptionThrown() {
        Long commentId = 1L;
        CommentRequest request = new CommentRequest();
        request.setContent("Updated Comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.updateComment(commentId, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Comment not found");

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testDeleteCommentWithExistingCommentExpectCommentDeletion() {
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testDeleteCommentWithNonExistingCommentExpectExceptionThrown() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Comment not found");

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any());
    }
}