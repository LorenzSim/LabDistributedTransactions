package be.ucll.da.appointmentservice.domain.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    void deleteById(long id);
    List<Appointment> getAppointmentsByDoctorIdEqualsAndPatientPreferredDayEquals(long id, LocalDate preferredDay);
    Optional<Appointment> getAppointmentByPatientFirstNameAndPatientLastNameAndPatientPreferredDay(String firstName, String lastName, LocalDate preferredDay);
}
