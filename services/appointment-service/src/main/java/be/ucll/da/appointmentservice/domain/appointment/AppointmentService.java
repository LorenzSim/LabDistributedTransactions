package be.ucll.da.appointmentservice.domain.appointment;

import be.ucll.da.appointmentservice.api.model.ApiAppointmentRequest;
import be.ucll.da.appointmentservice.messaging.MessageSender;
import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.doctorservice.api.messaging.model.DoctorOnPayroll;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsOnPayrollEvent;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MessageSender messageSender;

    @Autowired
    public AppointmentService( AppointmentRepository appointmentRepository, MessageSender messageSender) {
        this.appointmentRepository = appointmentRepository;
        this.messageSender = messageSender;
    }

    public Appointment getAppointment(long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new AppointmentServiceException("Appointment with id " + id + " not found!"));
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
        validatePatient(appointment.getId(), appointment.getPatientFirstName(), appointment.getPatientLastName());

        return appointment.getId().toString();
    }

    public void validatePatient(Long id, String firstName, String lastName) {
        ValidatePatientCommand command = new ValidatePatientCommand();
        command.id(id.intValue()).firstName(firstName).lastName(lastName);
        messageSender.sendValidatePatientCommand(command);
    }

    public void handlePatientValidatedEvent(PatientValidatedEvent event) {
        if (!event.getIsClient()) {
            cancelAppointment(event.getId());
            return;
        }
        Appointment appointment = getAppointment(event.getId());
        appointment.setAppointmentStatus(AppointmentStatus.PATIENT_IS_CHECKED);
        appointment = appointmentRepository.save(appointment);
        checkDoctorsEmployed(appointment.getPatientNeededExpertise());
    }

    public void checkDoctorsEmployed(String fieldOfExpertiseNeeded) {
        CheckDoctorsEmployedCommand command = new CheckDoctorsEmployedCommand().fieldOfExpertise(fieldOfExpertiseNeeded);
        messageSender.sendCheckDoctorsEmployedCommand(command);
    }

    public void cancelAppointment(long id) {
        appointmentRepository.deleteById(id);
    }

    public void handleDoctorsOnPayrollEvent(DoctorsOnPayrollEvent event) {
        List<DoctorOnPayroll> doctors = event.getDoctors();
     //   Appointment appointment = getAppointment()

        DoctorOnPayroll selectedDoctor = null;
        for (DoctorOnPayroll doctor : doctors) {
       //     List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorAndPatientPreferredDay(doctor.getId(), data.getPreferredDay());

   //         if (appointments.isEmpty()) {
    //            selectedDoctor = doctor;
    //            break;
        //    }
        }

        if (selectedDoctor == null) {
            throw new AppointmentServiceException("No doctor available for day and expertise");
        }



      //  appointmentRepository.save(appointment);
    }
}
