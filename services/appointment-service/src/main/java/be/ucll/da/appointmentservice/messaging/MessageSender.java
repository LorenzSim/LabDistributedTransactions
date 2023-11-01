package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.OpenPatientAccountCommand;
import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import be.ucll.da.roomservice.api.messaging.model.ReserveRoomCommand;

public interface MessageSender {
    void sendValidatePatientCommand(ValidatePatientCommand validatePatientCommand);
    void sendCheckDoctorsEmployedCommand(CheckDoctorsEmployedCommand  checkDoctorsEmployedCommand);
    void sendReserveRoomCommand(ReserveRoomCommand reserveRoomCommand);
    void sendOpenPatientAccountCommand(OpenPatientAccountCommand command);
}
