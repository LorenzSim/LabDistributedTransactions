package be.ucll.da.appointmentservice.domain.appointment;

import be.ucll.da.accountingservice.api.messaging.model.OpenPatientAccountCommand;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountCreatedEvent;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountNotCreatedEvent;
import be.ucll.da.appointmentservice.api.model.ApiAppointmentRequest;
import be.ucll.da.appointmentservice.messaging.MessageSender;
import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.doctorservice.api.messaging.model.DoctorEmployed;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.patientservice.api.messaging.model.ValidatePatientCommand;
import be.ucll.da.roomservice.api.messaging.model.ReserveRoomCommand;
import be.ucll.da.roomservice.api.messaging.model.RoomReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class AppointmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentService.class);

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

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(long id) {
        appointmentRepository.deleteById(id);
    }

    public String createAppointment(ApiAppointmentRequest request) {
        // save appointment to db
        Appointment appointment = new Appointment(request.getPatientFirstName(), request.getPatientLastName(), request.getNeededExpertise(), request.getPreferredDay());
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
        if (Boolean.FALSE.equals(event.getIsClient())) {
            cancelAppointment(event.getId());
            return;
        }
        Appointment appointment = updateStatus(event.getId(), AppointmentStatus.PATIENT_IS_CHECKED);
        checkDoctorsEmployed(appointment.getId(), appointment.getPatientNeededExpertise());
    }

    private void checkDoctorsEmployed(Long appointmentId, String fieldOfExpertiseNeeded) {
        CheckDoctorsEmployedCommand command = new CheckDoctorsEmployedCommand().fieldOfExpertise(fieldOfExpertiseNeeded).appointmentId(appointmentId.intValue());
        messageSender.sendCheckDoctorsEmployedCommand(command);
    }

    public void cancelAppointment(long id) {
        LOGGER.debug("Appointment with id " + id + " cancelled!");
        appointmentRepository.deleteById(id);
    }

    public Appointment updateStatus(long id, AppointmentStatus status) {
        Appointment appointment = getAppointment(id);
        return updateStatus(appointment, status);
    }

    public Appointment updateStatus(Appointment appointment, AppointmentStatus status) {
        appointment.setAppointmentStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void handleDoctorsEmployedEvent(DoctorsEmployedEvent event) {
        List<DoctorEmployed> doctors = event.getDoctors();
        Appointment appointment = getAppointment(event.getAppointmentId());

        DoctorEmployed selectedDoctor = null;
        for (DoctorEmployed doctor : doctors) {
            List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorIdEqualsAndPatientPreferredDayEquals(doctor.getId(), appointment.getPatientPreferredDay());
            if (appointments.isEmpty()) {
                selectedDoctor = doctor;
                break;
            }
        }

        if (selectedDoctor == null) {
            cancelAppointment(appointment.getId());
            return;
        }

        appointment.setAppointmentStatus(AppointmentStatus.DOCTOR_AVAILABLE);
        appointment.setDoctorId(selectedDoctor.getId());
        appointment = appointmentRepository.save(appointment);
        LOGGER.info(appointment.toString());

        checkAvailableRoom(appointment.getId(), appointment.getPatientPreferredDay());
    }

    public void selectDoctor(long appointmentId, List<DoctorEmployed> doctorsEmployed) {
        Appointment appointment = getAppointment(appointmentId);

        DoctorEmployed selectedDoctor = null;
        for (DoctorEmployed doctor : doctorsEmployed) {
            List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorIdEqualsAndPatientPreferredDayEquals(doctor.getId(), appointment.getPatientPreferredDay());
            if (appointments.isEmpty()) {
                selectedDoctor = doctor;
                break;
            }
        }

        if (selectedDoctor == null) {
            cancelAppointment(appointment.getId());
            return;
        }

        appointment.setAppointmentStatus(AppointmentStatus.DOCTOR_AVAILABLE);
        appointment.setDoctorId(selectedDoctor.getId());
        appointmentRepository.save(appointment);
    }

    private void checkAvailableRoom(Long appointMentId, LocalDate date){
        ReserveRoomCommand command = new ReserveRoomCommand().appointmentId(appointMentId.intValue()).day(date);
        messageSender.sendReserveRoomCommand(command);
    }

    public void handleRoomReservedEvent(RoomReservedEvent event) {
        if (event.getId().longValue() == -1) {
            cancelAppointment(event.getAppointmentId());
            return;
        }
        Appointment appointment = updateStatus(event.getAppointmentId(), AppointmentStatus.ROOM_AVAILABLE);
        appointment.setRoomId((long) event.getId());
        appointment = appointmentRepository.save(appointment);
        openAccount(appointment.getId().intValue(), new Random().nextInt(), appointment.getDoctorId().intValue(), appointment.getRoomId().intValue(), appointment.getPatientPreferredDay());
    }

    public void openAccount(Integer appointmentId, Integer patientId, Integer doctorId, Integer roomId, LocalDate dayOfAppointment) {
        OpenPatientAccountCommand command = new OpenPatientAccountCommand()
                .appointmentId(appointmentId)
                .patientId(patientId.intValue())
                .doctorId(doctorId)
                .roomId(roomId)
                .dayOfAppointment(dayOfAppointment);

        messageSender.sendOpenPatientAccountCommand(command);
    }


    public void handleAccountCreatedEvent(PatientAccountCreatedEvent event) {
        Appointment appointment = updateStatus(event.getAppointmentId(), AppointmentStatus.INSURANCE_INFORMED);
        appointment.setAccountId(event.getAccountId().longValue());
        appointmentRepository.save(appointment);
    }

    public void handleAccountNotCreatedEvent(PatientAccountNotCreatedEvent event) {
        cancelAppointment(event.getAppointmentId());
    }
}
