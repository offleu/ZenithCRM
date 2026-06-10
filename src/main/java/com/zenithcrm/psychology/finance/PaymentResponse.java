package com.zenithcrm.psychology.finance;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long id,
        Long patientId,
        String patientName,
        BigDecimal amount,
        LocalDate dueDate,
        LocalDate paidAt,
        PaymentStatus status,
        String method,
        String reference,
        String notes
) {
    static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPatient().getId(),
                payment.getPatient().getFullName(),
                payment.getAmount(),
                payment.getDueDate(),
                payment.getPaidAt(),
                effectiveStatus(payment),
                payment.getMethod(),
                payment.getReference(),
                payment.getNotes()
        );
    }

    private static PaymentStatus effectiveStatus(Payment payment) {
        if (payment.getStatus() == PaymentStatus.PENDING && payment.getDueDate().isBefore(LocalDate.now())) {
            return PaymentStatus.OVERDUE;
        }
        return payment.getStatus();
    }
}
