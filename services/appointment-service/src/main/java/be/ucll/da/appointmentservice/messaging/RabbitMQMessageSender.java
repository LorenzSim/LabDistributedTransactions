package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.OpenPatientAccountCommand;
import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import be.ucll.da.roomservice.api.messaging.model.ReserveRoomCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageSender implements MessageSender{
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendValidatePatientCommand(ValidatePatientCommand validatePatientCommand) {
        rabbitTemplate.convertAndSend("q.validate-patient", validatePatientCommand);
    }

    @Override
    public void sendCheckDoctorsEmployedCommand(CheckDoctorsEmployedCommand checkDoctorsEmployedCommand) {
        rabbitTemplate.convertAndSend("q.check-doctors-employed", checkDoctorsEmployedCommand);
    }

    @Override
    public void sendReserveRoomCommand(ReserveRoomCommand reserveRoomCommand) {
        rabbitTemplate.convertAndSend("q.reserve-room", reserveRoomCommand);
    }

    @Override
    public void sendOpenPatientAccountCommand(OpenPatientAccountCommand command) {
        rabbitTemplate.convertAndSend("q.open-account", command);
    }
}
