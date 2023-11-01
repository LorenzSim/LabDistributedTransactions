package be.ucll.da.accountingservice.domain;

import be.ucll.da.accountingservice.api.messaging.model.OpenPatientAccountCommand;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountCreatedEvent;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountNotCreatedEvent;
import be.ucll.da.accountingservice.messaging.RabbitMQMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountingService {

    private final RabbitMQMessageSender messageSender;

    @Autowired
    public AccountingService(RabbitMQMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    // Returns true if successful, false if patient is not insured
    public boolean openAccount(Long patientId) {
        return true;
    }

    public void handleOpenAccountCommand(OpenPatientAccountCommand command) {
        boolean accountIsOpen = openAccount(command.getPatientId().longValue());
        if (accountIsOpen) {
            PatientAccountCreatedEvent accountCreatedEvent = new PatientAccountCreatedEvent().appointmentId(command.getAppointmentId()).accountId(new Random().nextInt()).patientId(command.getPatientId()).dayOfAppointment(command.getDayOfAppointment());
            messageSender.sendAccountCreatedEvent(accountCreatedEvent);
        } else {
            PatientAccountNotCreatedEvent accountNotCreatedEvent = new PatientAccountNotCreatedEvent().appointmentId(command.getAppointmentId()).patientId(command.getPatientId()).dayOfAppointment(command.getDayOfAppointment()).error("NO insurance found!");
            messageSender.sendAccountNotCreatedEvent(accountNotCreatedEvent);
        }
    }
}
