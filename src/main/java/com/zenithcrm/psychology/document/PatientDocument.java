package com.zenithcrm.psychology.document;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_documents")
public class PatientDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser psychologist;

    @ManyToOne(optional = false)
    private Patient patient;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type = DocumentType.OTHER;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected PatientDocument() {
    }

    public PatientDocument(AppUser psychologist, Patient patient, PatientDocumentRequest request) {
        this.psychologist = psychologist;
        this.patient = patient;
        update(patient, request);
    }

    public void update(Patient patient, PatientDocumentRequest request) {
        this.patient = patient;
        this.title = request.title();
        this.type = request.type() == null ? DocumentType.OTHER : request.type();
        this.content = request.content();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public String getTitle() { return title; }
    public DocumentType getType() { return type; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
