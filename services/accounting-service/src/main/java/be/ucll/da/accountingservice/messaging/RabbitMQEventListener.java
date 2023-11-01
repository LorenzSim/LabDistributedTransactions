package be.ucll.da.accountingservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.OpenPatientAccountCommand;
import be.ucll.da.accountingservice.domain.AccountingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMQEventListener.class);
    private final AccountingService service;

    @Autowired
    public RabbitMQEventListener(AccountingService service) {
        this.service = service;
    }

    @RabbitListener(queues = "q.open-account")
    public void openAccount(OpenPatientAccountCommand command){
        LOGGER.info("Received: " + command);
        service.handleOpenAccountCommand(command);
    }

}
