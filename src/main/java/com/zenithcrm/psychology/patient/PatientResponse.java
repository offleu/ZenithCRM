package com.zenithcrm.psychology.patient;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String fullName,
        String phone,
        String email,
        LocalDate birthDate,
        String cpf,
        BigDecimal sessionValue,
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
                patient.getSessionValue(),
                patient.getClinicalNotes()
        );
    }
}
