package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;

public interface MessageSender {
    void sendValidatePatientCommand(ValidatePatientCommand validatePatientCommand);
    void sendCheckDoctorsEmployedCommand(CheckDoctorsEmployedCommand  checkDoctorsEmployedCommand);
}
