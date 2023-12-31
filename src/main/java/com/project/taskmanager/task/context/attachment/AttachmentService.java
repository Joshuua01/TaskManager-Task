package com.project.taskmanager.task.context.attachment;

import com.project.taskmanager.task.domain.Attachment;
import com.project.taskmanager.task.infrastructure.AttachmentRepository;
import com.project.taskmanager.task.infrastructure.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    private byte[] extractBytes (MultipartFile file){
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error while extracting bytes from file");
        }
    }

    public String uploadAttachment(MultipartFile file, Long taskId) {
        Attachment attachment = Attachment.builder()
                .name(file.getName())
                .type(file.getContentType())
                .data(extractBytes(file))
                .task(taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found")))
                .build();
        attachmentRepository.save(attachment);
        return "Attachment uploaded successfully";
    }

    public Attachment downloadAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
        return attachment;
    }

    public void deleteAttachment(Long attachmentId) {
        attachmentRepository.deleteById(attachmentId);
    }
}
