package be.ucll.da.doctorservice.domain;

import be.ucll.da.doctorservice.api.messaging.model.CheckDoctorsEmployedCommand;
import be.ucll.da.doctorservice.api.messaging.model.DoctorEmployed;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;
import be.ucll.da.doctorservice.messaging.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DoctorService {

    private final MessageSender messageSender;

    @Autowired
    public DoctorService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    private DoctorEmployed mapToDoctorEmployed(Doctor doctor) {
        return new DoctorEmployed()
                .id(doctor.id())
                .firstName(doctor.firstName())
                .lastName(doctor.lastName())
                .address(doctor.address())
                .age(doctor.age());
    }

    public void handleCheckDoctorsEmployedCommand(CheckDoctorsEmployedCommand command) {
        List<DoctorEmployed> doctors =
                getDoctorsEmployed(command.getFieldOfExpertise()).stream()
                        .map(this::mapToDoctorEmployed).collect(Collectors.toList());

        DoctorsEmployedEvent event =
                new DoctorsEmployedEvent()
                        .doctors(doctors)
                        .appointmentId(command.getAppointmentId())
                        .fieldOfExpertise(command.getFieldOfExpertise());

        messageSender.sendDoctorsEmployedEvent(event);

    }

    public List<Doctor> getDoctorsEmployed(String fieldOfExpertise) {
        if (fieldOfExpertise == null || fieldOfExpertise.isEmpty()) {
            return new ArrayList<>();
        }

        return getAllDoctors().stream()
                .filter(doctor -> doctor.fieldOfExpertise().equals(fieldOfExpertise))
                .collect(Collectors.toList());
    }

    private List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1L, "Cardiologie", "Juliette",
                "Tucker", 32, "Rue du Centre 259, 3000 Leuven, Belgium"));
        doctors.add(new Doctor(2L, "Dermatologie", "Preston",
                "Mueller", 45, "Avenue Emile Vandervelde 465, 3000 Leuven, Belgium"));
        doctors.add(new Doctor(1L, "Gynaecologie", "Katrina",
                "Mendoza", 67, "Kapelaniestraat 94 22, 3000 Leuven, Belgium"));

        return doctors;
    }
}
