package com.zenithcrm.psychology.finance;

import com.zenithcrm.psychology.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPsychologistAndDueDateBetweenOrderByDueDate(AppUser psychologist, LocalDate from, LocalDate to);
    Optional<Payment> findByIdAndPsychologist(Long id, AppUser psychologist);
}
