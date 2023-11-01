package be.ucll.da.doctorservice.messaging;

import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqMessageSender implements MessageSender{
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendDoctorsEmployedEvent(DoctorsEmployedEvent event) {
        rabbitTemplate.convertAndSend("x.doctors-employed-checked", "", event);
    }
}
