package com.zenithcrm.psychology.appointment;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.*;

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

    @Column(length = 2000)
    private String notes;

    protected Appointment() {
    }

    public Appointment(AppUser psychologist, Patient patient, AppointmentRequest request) {
        this.psychologist = psychologist;
        this.patient = patient;
        update(patient, request);
    }

    public void update(Patient patient, AppointmentRequest request) {
        this.patient = patient;
        this.startsAt = request.startsAt();
        this.endsAt = request.endsAt();
        this.status = request.status() == null ? AppointmentStatus.SCHEDULED : request.status();
        this.location = request.location();
        this.notes = request.notes();
        this.googleEventId = request.googleEventId();
    }

    public Long getId() { return id; }
    public AppUser getPsychologist() { return psychologist; }
    public Patient getPatient() { return patient; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public AppointmentStatus getStatus() { return status; }
    public String getLocation() { return location; }
    public String getGoogleEventId() { return googleEventId; }
    public String getNotes() { return notes; }
}
