package com.zenithcrm.psychology.finance;

import com.zenithcrm.psychology.patient.Patient;
import com.zenithcrm.psychology.patient.PatientRepository;
import com.zenithcrm.psychology.security.CurrentUserService;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class FinanceController {
    private final PaymentRepository payments;
    private final PatientRepository patients;
    private final CurrentUserService currentUserService;

    public FinanceController(PaymentRepository payments, PatientRepository patients, CurrentUserService currentUserService) {
        this.payments = payments;
        this.patients = patients;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    List<PaymentResponse> list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        AppUser user = currentUserService.currentUser();
        LocalDate start = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate end = to == null ? LocalDate.now().plusMonths(1) : to;
        return payments.findByPsychologistAndDueDateBetweenOrderByDueDate(user, start, end)
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @GetMapping("/summary")
    FinanceSummary summary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<PaymentResponse> records = list(from, to);
        BigDecimal received = sum(records, PaymentStatus.PAID);
        BigDecimal pending = sum(records, PaymentStatus.PENDING);
        BigDecimal overdue = sum(records, PaymentStatus.OVERDUE);
        return new FinanceSummary(
                received,
                pending,
                overdue,
                records.stream().filter(p -> p.status() == PaymentStatus.PAID).count(),
                records.stream().filter(p -> p.status() == PaymentStatus.PENDING).count(),
                records.stream().filter(p -> p.status() == PaymentStatus.OVERDUE).count()
        );
    }

    @PostMapping
    PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        AppUser user = currentUserService.currentUser();
        return PaymentResponse.from(payments.save(new Payment(user, patientFor(user, request.patientId()), request)));
    }

    @PutMapping("/{id}")
    PaymentResponse update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) {
        AppUser user = currentUserService.currentUser();
        Payment payment = payments.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento nao encontrado."));
        payment.update(patientFor(user, request.patientId()), request);
        return PaymentResponse.from(payments.save(payment));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        AppUser user = currentUserService.currentUser();
        Payment payment = payments.findByIdAndPsychologist(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento nao encontrado."));
        payments.delete(payment);
    }

    private BigDecimal sum(List<PaymentResponse> records, PaymentStatus status) {
        return records.stream()
                .filter(payment -> payment.status() == status)
                .map(PaymentResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Patient patientFor(AppUser user, Long patientId) {
        return patients.findByIdAndPsychologist(patientId, user)
                .orElseThrow(() -> new EntityNotFoundException("Paciente nao encontrado."));
    }
}
