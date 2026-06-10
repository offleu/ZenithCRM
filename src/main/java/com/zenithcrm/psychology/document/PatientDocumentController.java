package com.zenithcrm.psychology.document;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.patient.PatientRepository;
import com.zenithcrm.psychology.security.CurrentUserService;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class PatientDocumentController {
    private final PatientDocumentRepository documents;
    private final PatientRepository patients;
    private final CurrentUserService currentUserService;

    public PatientDocumentController(PatientDocumentRepository documents, PatientRepository patients, CurrentUserService currentUserService) {
        this.documents = documents;
        this.patients = patients;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    List<PatientDocumentResponse> list() {
        return documents.findByPsychologistOrderByUpdatedAtDesc(currentUserService.currentUser())
                .stream()
                .map(PatientDocumentResponse::from)
                .toList();
    }

    @PostMapping
    PatientDocumentResponse create(@Valid @RequestBody PatientDocumentRequest request) {
        AppUser user = currentUserService.currentUser();
        return PatientDocumentResponse.from(documents.save(new PatientDocument(user, patientFor(user, request.patientId()), request)));
    }

    @PutMapping("/{id}")
    PatientDocumentResponse update(@PathVariable Long id, @Valid @RequestBody PatientDocumentRequest request) {
        AppUser user = currentUserService.currentUser();
        PatientDocument document = documents.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Documento nao encontrado."));
        document.update(patientFor(user, request.patientId()), request);
        return PatientDocumentResponse.from(documents.save(document));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        AppUser user = currentUserService.currentUser();
        PatientDocument document = documents.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Documento nao encontrado."));
        documents.delete(document);
    }

    private Patient patientFor(AppUser user, Long patientId) {
        return patients.findByIdAndPsychologist(patientId, user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente nao encontrado."));
    }
}
