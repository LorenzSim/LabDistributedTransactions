package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.PatientAccountCreatedEvent;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountNotCreatedEvent;
import be.ucll.da.appointmentservice.domain.appointment.AppointmentService;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.roomservice.api.messaging.model.RoomReservedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventListener implements EventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqEventListener.class);

    private final AppointmentService appointmentService;

    @Autowired
    public RabbitMqEventListener(AppointmentService service) {
        this.appointmentService = service;
    }

    @Override
    @RabbitListener(queues = "q.appointment-validate-patient-reply")
    public void patientValidated(@NotNull PatientValidatedEvent event) {
        LOGGER.info("\nReceived " + event);
        appointmentService.handlePatientValidatedEvent(event);
    }

    @Override
    @RabbitListener(queues = "q.appointment-doctors-employed-reply")
    public void doctorsEmployed(@NotNull DoctorsEmployedEvent event) {
        LOGGER.info("\nReceived " + event);
        appointmentService.handleDoctorsEmployedEvent(event);
    }

    @Override
    @RabbitListener(queues = "q.appointment-rooms-reserve-reply")
    public void roomReserved(@NotNull RoomReservedEvent event) {
        LOGGER.info("\nReceived " + event);
        appointmentService.handleRoomReservedEvent(event);
    }

    @Override
    @RabbitListener(queues = "q.appointment-accounting-account-created-reply")
    public void accountCreated(@NotNull PatientAccountCreatedEvent event) {
        LOGGER.info("\nReceived " + event);
        appointmentService.handleAccountCreatedEvent(event);
    }

    @Override
    @RabbitListener(queues = "q.appointment-accounting-account-not-created-reply")
    public void accountNotCreated(@NotNull PatientAccountNotCreatedEvent event) {
        LOGGER.info("\nReceived " + event);
        appointmentService.handleAccountNotCreatedEvent(event);

    }


}
