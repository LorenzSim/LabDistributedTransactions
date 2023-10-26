package be.ucll.da.appointmentservice.domain.appointment;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String patientFirstName;
    private String patientLastName;
    private String patientNeededExpertise;
    private LocalDate patientPreferredDay;
    private AppointmentStatus appointmentStatus;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getPatientNeededExpertise() {
        return patientNeededExpertise;
    }

    public void setPatientNeededExpertise(String neededExpertise) {
        this.patientNeededExpertise = neededExpertise;
    }

    public LocalDate getPatientPreferredDay() {
        return patientPreferredDay;
    }

    public void setPatientPreferredDay(LocalDate preferredDay) {
        this.patientPreferredDay = preferredDay;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
