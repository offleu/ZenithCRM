package com.zenithcrm.psychology.appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        AppointmentStatus status,
        String location,
        String notes,
        String googleEventId,
        String googleCalendarUrl
) {
}
