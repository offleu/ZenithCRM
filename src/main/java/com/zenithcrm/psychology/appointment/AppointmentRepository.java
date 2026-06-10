package com.zenithcrm.psychology.appointment;

import com.zenithcrm.psychology.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPsychologistAndStartsAtBetweenOrderByStartsAt(AppUser psychologist, LocalDateTime from, LocalDateTime to);
    Optional<Appointment> findByIdAndPsychologist(Long id, AppUser psychologist);
}
