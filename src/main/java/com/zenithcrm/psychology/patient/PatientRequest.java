package com.zenithcrm.psychology.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PatientRequest(
        @NotBlank String fullName,
        String phone,
        String email,
        LocalDate birthDate,
        String cpf,
        @NotNull @Positive BigDecimal sessionValue,
        String clinicalNotes
) {
}
