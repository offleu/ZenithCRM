package com.zenithcrm.psychology.patient;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank String fullName,
        String phone,
        String email,
        LocalDate birthDate,
        String cpf,
        String clinicalNotes
) {
}
