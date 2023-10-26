package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;

public interface MessageSender {
    void sendValidatePatientCommand(ValidatePatientCommand validatePatientCommand);
}
