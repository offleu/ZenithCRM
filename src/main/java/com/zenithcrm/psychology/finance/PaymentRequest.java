package com.zenithcrm.psychology.finance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequest(
        @NotNull Long patientId,
        @NotNull @Positive BigDecimal amount,
        @NotNull LocalDate dueDate,
        LocalDate paidAt,
        PaymentStatus status,
        String method,
        String reference,
        String notes
) {
}
