package be.ucll.da.patientservice.messaging;

import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqMessageSender implements MessageSender {
    private final RabbitTemplate template;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void sendPatientValidatedEvent(PatientValidatedEvent event) {
        template.convertAndSend("x.patient-validated", "", event);
    }
}
