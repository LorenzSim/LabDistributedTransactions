package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.appointmentservice.domain.appointment.AppointmentService;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsOnPayrollEvent;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventListener {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqEventListener.class);

    private final AppointmentService appointmentService;

    @Autowired
    public RabbitMqEventListener(AppointmentService service) {
        this.appointmentService = service;
    }

    @RabbitListener(queues = "q.appointment-validate-patient-reply")
    public void patientValidated(PatientValidatedEvent event) {
        logger.info("\nReceived " + event.toString());
        appointmentService.handlePatientValidatedEvent(event);
    }

    @RabbitListener(queues = "q.appointment-doctors-on-payroll-reply")
    public void doctorsOnPayrollChecked(DoctorsOnPayrollEvent event) {
        logger.info("\nReceived " + event.toString());
        appointmentService.handleDoctorsOnPayrollEvent(event);
    }



}
