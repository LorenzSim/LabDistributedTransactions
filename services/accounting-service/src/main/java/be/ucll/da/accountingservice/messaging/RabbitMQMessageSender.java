package be.ucll.da.accountingservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.PatientAccountCreatedEvent;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountNotCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageSender {
    private final RabbitTemplate template;

    @Autowired
    public RabbitMQMessageSender(RabbitTemplate template) {
        this.template = template;
    }

    public void sendAccountCreatedEvent(PatientAccountCreatedEvent event) {
        template.convertAndSend("x.account-created", "", event);
    }
    public void sendAccountNotCreatedEvent(PatientAccountNotCreatedEvent event) {
        template.convertAndSend("x.account-not-created", "", event);
    }
}
