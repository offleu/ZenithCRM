package com.zenithcrm.psychology.patient;

import com.zenithcrm.psychology.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByPsychologistOrderByFullName(AppUser psychologist);
    Optional<Patient> findByIdAndPsychologist(Long id, AppUser psychologist);
}
