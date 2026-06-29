package com.zenithcrm.psychology.finance;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser psychologist;

    @ManyToOne(optional = false)
    private Patient patient;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String method;
    private String reference;

    @Column(length = 1000)
    private String notes;

    protected Payment() {
    }

    public Payment(AppUser psychologist, Patient patient, PaymentRequest request) {
        this.psychologist = psychologist;
        this.patient = patient;
        update(patient, request);
    }

    public Payment(AppUser psychologist, Patient patient, BigDecimal amount, LocalDate dueDate, String reference, String notes) {
        this.psychologist = psychologist;
        this.patient = patient;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = PaymentStatus.PENDING;
        this.reference = reference;
        this.notes = notes;
    }

    public void update(Patient patient, PaymentRequest request) {
        this.patient = patient;
        this.amount = request.amount();
        this.dueDate = request.dueDate();
        this.paidAt = request.paidAt();
        this.status = request.status() == null ? PaymentStatus.PENDING : request.status();
        this.method = request.method();
        this.reference = request.reference();
        this.notes = request.notes();
    }

    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getPaidAt() { return paidAt; }
    public PaymentStatus getStatus() { return status; }
    public String getMethod() { return method; }
    public String getReference() { return reference; }
    public String getNotes() { return notes; }
}
