package com.zenithcrm.psychology.patient;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String fullName,
        String phone,
        String email,
        LocalDate birthDate,
        String cpf,
        String clinicalNotes
) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getCpf(),
                patient.getClinicalNotes()
        );
    }
}
