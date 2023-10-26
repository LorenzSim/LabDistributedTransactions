package be.ucll.da.patientservice.messaging;

import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;

public interface MessageSender {
    void sendPatientValidatedEvent(PatientValidatedEvent event);
}
