package com.zenithcrm.psychology.appointment;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.patient.PatientRepository;
import com.zenithcrm.psychology.security.CurrentUserService;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    private final CurrentUserService currentUserService;
    private final GoogleCalendarService googleCalendarService;
    private final AppointmentWorkflowService workflowService;

    public AppointmentController(AppointmentRepository appointments,
                                 PatientRepository patients,
                                 CurrentUserService currentUserService,
                                 GoogleCalendarService googleCalendarService,
                                 AppointmentWorkflowService workflowService) {
        this.appointments = appointments;
        this.patients = patients;
        this.currentUserService = currentUserService;
        this.googleCalendarService = googleCalendarService;
        this.workflowService = workflowService;
    }

    @GetMapping
    List<AppointmentResponse> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        AppUser user = currentUserService.currentUser();
        LocalDate start = from == null ? LocalDate.now().minusDays(7) : from;
        LocalDate end = to == null ? LocalDate.now().plusDays(30) : to;
        return appointments.findByPsychologistAndStartsAtBetweenOrderByStartsAt(user, start.atStartOfDay(), end.plusDays(1).atStartOfDay())
                .stream()
                .map(this::response)
                .toList();
    }

    @GetMapping("/day")
    List<AppointmentResponse> day(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AppUser user = currentUserService.currentUser();
        LocalDate selectedDate = date == null ? LocalDate.now() : date;
        return appointments.findByPsychologistAndStartsAtBetweenOrderByStartsAt(
                        user,
                        selectedDate.atStartOfDay(),
                        selectedDate.plusDays(1).atStartOfDay()
                )
                .stream()
                .map(this::response)
                .toList();
    }

    @PostMapping
    AppointmentResponse create(@Valid @RequestBody AppointmentRequest request) {
        AppUser user = currentUserService.currentUser();
        Patient patient = patientFor(user, request.patientId());
        return response(appointments.save(new Appointment(user, patient, request)));
    }

    @PutMapping("/{id}")
    AppointmentResponse update(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
        AppUser user = currentUserService.currentUser();
        Appointment appointment = appointments.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nao encontrado."));
        appointment.update(patientFor(user, request.patientId()), request);
        return response(appointments.save(appointment));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        AppUser user = currentUserService.currentUser();
        Appointment appointment = appointments.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nao encontrado."));
        appointments.delete(appointment);
    }

    @PostMapping("/{id}/confirm")
    AppointmentResponse confirm(@PathVariable Long id) {
        return response(workflowService.confirm(id, currentUserService.currentUser()));
    }

    @PostMapping("/{id}/start")
    AppointmentResponse start(@PathVariable Long id) {
        return response(workflowService.start(id, currentUserService.currentUser()));
    }

    @PostMapping("/{id}/finish")
    AppointmentResponse finish(@PathVariable Long id) {
        return response(workflowService.finish(id, currentUserService.currentUser()));
    }

    @PostMapping("/{id}/absence")
    AppointmentResponse absence(@PathVariable Long id) {
        return response(workflowService.registerAbsence(id, currentUserService.currentUser()));
    }

    private Patient patientFor(AppUser user, Long patientId) {
        return patients.findByIdAndPsychologist(patientId, user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente nao encontrado."));
    }

    private AppointmentResponse response(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getFullName(),
                appointment.getPatient().getSessionValue(),
                appointment.getStartsAt(),
                appointment.getEndsAt(),
                appointment.getStatus(),
                appointment.getLocation(),
                appointment.getNotes(),
                appointment.getGoogleEventId(),
                appointment.getStartedAt(),
                appointment.getFinishedAt(),
                appointment.getDurationMinutes(),
                appointment.getAbsenceRegisteredAt(),
                appointment.getGeneratedPayment() == null ? null : appointment.getGeneratedPayment().getId(),
                googleCalendarService.createEventUrl(appointment)
        );
    }
}
