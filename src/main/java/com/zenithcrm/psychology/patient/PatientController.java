package com.zenithcrm.psychology.patient;

import com.zenithcrm.psychology.security.CurrentUserService;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientRepository patients;
    private final CurrentUserService currentUserService;

    public PatientController(PatientRepository patients, CurrentUserService currentUserService) {
        this.patients = patients;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    List<PatientResponse> list() {
        return patients.findByPsychologistOrderByFullName(currentUserService.currentUser())
                .stream()
                .map(PatientResponse::from)
                .toList();
    }

    @PostMapping
    PatientResponse create(@Valid @RequestBody PatientRequest request) {
        return PatientResponse.from(patients.save(new Patient(currentUserService.currentUser(), request)));
    }

    @PutMapping("/{id}")
    PatientResponse update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        AppUser user = currentUserService.currentUser();
        Patient patient = patients.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado."));
        patient.update(request);
        return PatientResponse.from(patients.save(patient));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        AppUser user = currentUserService.currentUser();
        Patient patient = patients.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado."));
        patients.delete(patient);
    }
}
