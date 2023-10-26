package be.ucll.da.doctorservice.messaging;

import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @RabbitListener(queues = "q.check-doctors-employed")
    public void checkAvailableDoctors(CheckDoctorsEmployedCommand command) {
        LOGGER.info("\nReceived checkDoctorsEmployedCommand: " + command.toString());

    }
}
