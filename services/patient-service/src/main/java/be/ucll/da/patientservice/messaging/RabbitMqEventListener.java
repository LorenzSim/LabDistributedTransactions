package be.ucll.da.patientservice.messaging;

import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import be.ucll.da.patientservice.domain.PatientService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqEventListener {
    private final static Logger logger = LoggerFactory.getLogger(RabbitMqEventListener.class);
    private final PatientService patientService;

    @Autowired
    public RabbitMqEventListener(PatientService patientService) {
        this.patientService = patientService;
    }

    @RabbitListener(queues = "q.validate-patient")
    public void validatePatient(ValidatePatientCommand command) {
        logger.info("\nReceived validate patient command: " + command.toString());
        patientService.validatePatient(command.getId().longValue(), command.getFirstName(), command.getLastName());
    }
}
