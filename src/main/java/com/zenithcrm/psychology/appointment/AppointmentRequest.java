package com.zenithcrm.psychology.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull Long patientId,
        @NotNull LocalDateTime startsAt,
        @NotNull LocalDateTime endsAt,
        AppointmentStatus status,
        String location,
        String notes,
        String googleEventId
) {
}
