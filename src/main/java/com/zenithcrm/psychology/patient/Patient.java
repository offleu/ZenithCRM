package com.zenithcrm.psychology.patient;

import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser psychologist;

    @Column(nullable = false)
    private String fullName;

    private String phone;
    private String email;
    private LocalDate birthDate;
    private String cpf;

    @Column(precision = 12, scale = 2)
    private BigDecimal sessionValue = BigDecimal.ZERO;

    @Column(length = 2000)
    private String clinicalNotes;

    protected Patient() {
    }

    public Patient(AppUser psychologist, PatientRequest request) {
        this.psychologist = psychologist;
        update(request);
    }

    public void update(PatientRequest request) {
        this.fullName = request.fullName();
        this.phone = request.phone();
        this.email = request.email();
        this.birthDate = request.birthDate();
        this.cpf = request.cpf();
        this.sessionValue = request.sessionValue();
        this.clinicalNotes = request.clinicalNotes();
    }

    public Long getId() { return id; }
    public AppUser getPsychologist() { return psychologist; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getCpf() { return cpf; }
    public BigDecimal getSessionValue() { return sessionValue == null ? BigDecimal.ZERO : sessionValue; }
    public String getClinicalNotes() { return clinicalNotes; }
}
