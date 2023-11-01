package be.ucll.da.roomservice.messaging;

import be.ucll.da.roomservice.api.messaging.model.RoomReservedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageSender {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRoomReservedEvent(RoomReservedEvent event) {
        rabbitTemplate.convertAndSend("x.room-reserved", "", event);
    }

}
