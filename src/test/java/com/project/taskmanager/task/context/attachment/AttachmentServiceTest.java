package com.project.taskmanager.task.context.attachment;

import com.project.taskmanager.task.domain.Attachment;
import com.project.taskmanager.task.domain.Task;
import com.project.taskmanager.task.infrastructure.AttachmentRepository;
import com.project.taskmanager.task.infrastructure.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttachmentService attachmentService;
    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        attachmentService = new AttachmentService(attachmentRepository, taskRepository);
    }


    @Test
    void uploadAttachmentWithValidAttachmentAndTaskExpectNoError() {
        Long taskId = 1L;
        MultipartFile file = new MockMultipartFile("test-file.txt", "Hello, World!".getBytes());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mock(Task.class)));

        String result = attachmentService.uploadAttachment(file, taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(attachmentRepository, times(1)).save(any(Attachment.class));
        assertThat(result).isEqualTo("Attachment uploaded successfully");
    }

    @Test
    void testDownloadAttachmentWithExistingAttachmentExpectAttachment() {
        Long attachmentId = 1L;
        Attachment attachment = new Attachment();
        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachment));

        Attachment result = attachmentService.downloadAttachment(attachmentId);

        verify(attachmentRepository, times(1)).findById(attachmentId);
        assertThat(result).isEqualTo(attachment);
    }

    @Test
    void testDeleteAttachmentWithExistingAttachmentExpectAttachmentDelete() {
        Long attachmentId = 1L;

        attachmentService.deleteAttachment(attachmentId);

        verify(attachmentRepository, times(1)).deleteById(attachmentId);
    }
}