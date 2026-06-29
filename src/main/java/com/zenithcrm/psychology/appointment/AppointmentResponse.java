package com.zenithcrm.psychology.appointment;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        BigDecimal sessionValue,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        AppointmentStatus status,
        String location,
        String notes,
        String googleEventId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        LocalDateTime absenceRegisteredAt,
        Long generatedPaymentId,
        String googleCalendarUrl
) {
}
