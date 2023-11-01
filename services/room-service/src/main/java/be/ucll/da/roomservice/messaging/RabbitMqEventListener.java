package be.ucll.da.roomservice.messaging;

import be.ucll.da.roomservice.api.messaging.model.ReserveRoomCommand;
import be.ucll.da.roomservice.domain.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqEventListener.class);

    private final RoomService roomService;

    @Autowired
    public RabbitMqEventListener(RoomService roomService) {
        this.roomService = roomService;
    }

    @RabbitListener(queues = "q.reserve-room")
    public void reserveRoom(ReserveRoomCommand command) {
        LOGGER.info("\nReceived: " + command);
        roomService.handleReserveRoomCommand(command);
    }
}
