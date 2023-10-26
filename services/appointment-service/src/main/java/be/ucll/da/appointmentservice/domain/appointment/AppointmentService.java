package be.ucll.da.appointmentservice.domain.appointment;

import be.ucll.da.appointmentservice.api.model.ApiAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentSaga appointmentSaga;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentSaga saga, AppointmentRepository appointmentRepository) {
        this.appointmentSaga = saga;
        this.appointmentRepository = appointmentRepository;
    }

    public void cancelAppointment(long id) {
        appointmentRepository.deleteById(id);
    }

    public String createAppointment(ApiAppointmentRequest request) {
        // save appointment to db
        Appointment appointment = new Appointment();
        appointment.setPatientFirstName(request.getPatientFirstName());
        appointment.setPatientLastName(request.getPatientLastName());
        appointment.setPatientNeededExpertise(request.getNeededExpertise());
        appointment.setPatientPreferredDay(request.getPreferredDay());
        appointment.setAppointmentStatus(AppointmentStatus.CREATED);

        appointment = appointmentRepository.save(appointment);

        // check if doctor is available
        appointmentSaga.validatePatient(appointment.getId(), appointment.getPatientFirstName(), appointment.getPatientLastName());

        return appointment.getId().toString();
    }
}
