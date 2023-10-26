package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.appointmentservice.domain.appointment.AppointmentSaga;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqEventListener.class);

    private final AppointmentSaga appointmentSaga;

    @Autowired
    public RabbitMqEventListener(AppointmentSaga appointmentSaga) {
        this.appointmentSaga = appointmentSaga;
    }

    @RabbitListener(queues = "q.appointment-validate-patient-reply")
    public void patientValidated(PatientValidatedEvent event) {
        logger.info("\nReceived patient validated Event: " + event.toString());
        appointmentSaga.handlePatientValidatedEvent(event);
    }
}
