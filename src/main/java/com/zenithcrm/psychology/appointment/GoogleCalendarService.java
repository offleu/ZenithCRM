package com.zenithcrm.psychology.appointment;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class GoogleCalendarService {
    private static final DateTimeFormatter GOOGLE_DATE = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public String createEventUrl(Appointment appointment) {
        ZoneId zone = ZoneId.systemDefault();
        String dates = appointment.getStartsAt().atZone(zone).format(GOOGLE_DATE)
                + "/" + appointment.getEndsAt().atZone(zone).format(GOOGLE_DATE);
        return UriComponentsBuilder.fromUriString("https://calendar.google.com/calendar/render")
                .queryParam("action", "TEMPLATE")
                .queryParam("text", "Atendimento - " + appointment.getPatient().getFullName())
                .queryParam("dates", dates)
                .queryParam("details", appointment.getNotes() == null ? "" : appointment.getNotes())
                .queryParam("location", appointment.getLocation() == null ? "" : appointment.getLocation())
                .build()
                .toUriString();
    }
}
