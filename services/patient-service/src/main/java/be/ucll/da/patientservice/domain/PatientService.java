package be.ucll.da.patientservice.domain;

import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.patientservice.messaging.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final MessageSender messageSender;

    @Autowired
    public PatientService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void validatePatient(Long id, String firstName, String lastName) {
        String email = firstName + "." + lastName + "@google.com";

        boolean isClient = true;

        PatientValidatedEvent patientValidatedEvent = new PatientValidatedEvent();
        patientValidatedEvent
                .id(id.intValue())
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .isClient(isClient);

        messageSender.sendPatientValidatedEvent(patientValidatedEvent);
    }
}
