package com.zenithcrm.psychology.document;

import com.zenithcrm.psychology.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {
    List<PatientDocument> findByPsychologistOrderByUpdatedAtDesc(AppUser psychologist);
    Optional<PatientDocument> findByIdAndPsychologist(Long id, AppUser psychologist);
}
