package be.ucll.da.appointmentservice.domain.appointment;

import be.ucll.da.appointmentservice.messaging.MessageSender;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentSaga implements AppointmentCommander {
    private final MessageSender messageSender;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentSaga(MessageSender messageSender, AppointmentService appointmentService) {
        this.messageSender = messageSender;
        this.appointmentService = appointmentService;
    }

    public void validatePatient(Long id, String firstName, String lastName) {
        ValidatePatientCommand command = new ValidatePatientCommand();
        command.id(id.intValue()).firstName(firstName).lastName(lastName);
        messageSender.sendValidatePatientCommand(command);
    }


}
