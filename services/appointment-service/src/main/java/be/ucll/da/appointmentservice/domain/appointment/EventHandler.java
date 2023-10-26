package be.ucll.da.appointmentservice.domain.appointment;

import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {
    private final AppointmentService appointmentService;

    @Autowired
    public EventHandler(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void handlePatientValidatedEvent(PatientValidatedEvent event) {
        if (!event.getIsClient()) {
            cancelAppointment(event.getId());
        } else {
            appointmentService.
        }
    }

    public void cancelAppointment(long id) {
        appointmentService.cancelAppointment(id);
    }


}
