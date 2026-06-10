package com.zenithcrm.psychology.document;

import java.time.LocalDateTime;

public record PatientDocumentResponse(
        Long id,
        Long patientId,
        String patientName,
        String title,
        DocumentType type,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static PatientDocumentResponse from(PatientDocument document) {
        return new PatientDocumentResponse(
                document.getId(),
                document.getPatient().getId(),
                document.getPatient().getFullName(),
                document.getTitle(),
                document.getType(),
                document.getContent(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
