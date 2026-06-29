package com.zenithcrm.psychology.appointment;

import com.zenithcrm.psychology.finance.Payment;
import com.zenithcrm.psychology.finance.PaymentRepository;
import com.zenithcrm.psychology.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class AppointmentWorkflowService {
    private final AppointmentRepository appointments;
    private final PaymentRepository payments;

    public AppointmentWorkflowService(AppointmentRepository appointments, PaymentRepository payments) {
        this.appointments = appointments;
        this.payments = payments;
    }

    @Transactional
    public Appointment start(Long appointmentId, AppUser user) {
        Appointment appointment = appointmentFor(appointmentId, user);
        appointment.start(LocalDateTime.now());
        return appointments.save(appointment);
    }

    @Transactional
    public Appointment finish(Long appointmentId, AppUser user) {
        Appointment appointment = appointmentFor(appointmentId, user);
        if (appointment.getPatient().getSessionValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Paciente sem valor de sessao cadastrado.");
        }
        Payment payment = payments.save(new Payment(
                user,
                appointment.getPatient(),
                appointment.getPatient().getSessionValue(),
                appointment.getStartsAt().toLocalDate(),
                "APPOINTMENT-" + appointment.getId(),
                "Lancamento automatico do atendimento de " + appointment.getPatient().getFullName()
        ));
        appointment.finish(LocalDateTime.now(), payment);
        return appointments.save(appointment);
    }

    @Transactional
    public Appointment registerAbsence(Long appointmentId, AppUser user) {
        Appointment appointment = appointmentFor(appointmentId, user);
        appointment.registerAbsence(LocalDateTime.now());
        return appointments.save(appointment);
    }

    private Appointment appointmentFor(Long appointmentId, AppUser user) {
        return appointments.findByIdAndPsychologist(appointmentId, user)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento nao encontrado."));
    }
}
