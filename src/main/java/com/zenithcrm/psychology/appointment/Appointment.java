package com.zenithcrm.psychology.appointment;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.finance.Payment;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser psychologist;

    @ManyToOne(optional = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    private String location;
    private String googleEventId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Integer durationMinutes;

    private LocalDateTime absenceRegisteredAt;

    @OneToOne
    private Payment generatedPayment;

    @Column(length = 2000)
    private String notes;

    protected Appointment() {
    }

    public Appointment(AppUser psychologist, Patient patient, AppointmentRequest request) {
        this.psychologist = psychologist;
        this.patient = patient;
        updateSchedule(patient, request, true);
    }

    public void update(Patient patient, AppointmentRequest request) {
        updateSchedule(patient, request, false);
    }

    private void updateSchedule(Patient patient, AppointmentRequest request, boolean creating) {
        this.patient = patient;
        this.startsAt = request.startsAt();
        this.endsAt = request.endsAt();
        if (request.status() == null) {
            this.status = creating ? AppointmentStatus.SCHEDULED : this.status;
        } else if (request.status() == AppointmentStatus.COMPLETED
                || request.status() == AppointmentStatus.IN_PROGRESS
                || request.status() == AppointmentStatus.ABSENT) {
            throw new IllegalArgumentException("Use as ações do atendimento para iniciar, finalizar ou registrar ausência.");
        } else {
            this.status = request.status();
        }
        this.location = request.location();
        this.notes = request.notes();
        this.googleEventId = request.googleEventId();
    }

    public void start(LocalDateTime startedAt) {

        if (status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Atendimento já finalizado.");
        }

        if (status == AppointmentStatus.ABSENT) {
            throw new IllegalStateException("Atendimento marcado como ausência.");
        }

        if (status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Confirme o atendimento antes de iniciá-lo.");
        }
        this.startedAt = startedAt;
        this.status = AppointmentStatus.IN_PROGRESS;
    }

    public void finish(LocalDateTime finishedAt, Payment payment) {
        if (status == AppointmentStatus.ABSENT) {
            throw new IllegalStateException("Atendimento com ausência não pode ser finalizado.");
        }
        if (generatedPayment != null) {
            throw new IllegalStateException("Financeiro deste atendimento já foi lancado.");
        }
        if (startedAt == null) {
            throw new IllegalStateException("Inicie o atendimento antes de finalizar.");
        }
        this.finishedAt = finishedAt;

        this.durationMinutes = (int) Duration
                .between(startedAt, finishedAt)
                .toMinutes();

        this.generatedPayment = payment;
        this.status = AppointmentStatus.COMPLETED;
    }

    public void registerAbsence(LocalDateTime absenceRegisteredAt) {
        if (status == AppointmentStatus.COMPLETED || generatedPayment != null) {
            throw new IllegalStateException("Atendimento finalizado nao pode ser marcado como ausencia.");
        }
        this.absenceRegisteredAt = absenceRegisteredAt;
        this.status = AppointmentStatus.ABSENT;
    }


    public void confirm() {

        if(status != AppointmentStatus.SCHEDULED){

            throw new IllegalStateException("...");

        }

        this.status = AppointmentStatus.CONFIRMED;

    }




    public Long getId() { return id; }
    public AppUser getPsychologist() { return psychologist; }
    public Patient getPatient() { return patient; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public AppointmentStatus getStatus() { return status; }
    public String getLocation() { return location; }
    public String getGoogleEventId() { return googleEventId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public LocalDateTime getAbsenceRegisteredAt() { return absenceRegisteredAt; }
    public Payment getGeneratedPayment() { return generatedPayment; }
    public String getNotes() { return notes; }
    public Integer getDurationMinutes() {return durationMinutes;}
}
