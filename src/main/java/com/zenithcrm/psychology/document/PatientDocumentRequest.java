package com.zenithcrm.psychology.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientDocumentRequest(
        @NotNull Long patientId,
        @NotBlank String title,
        DocumentType type,
        @NotBlank String content
) {
}
