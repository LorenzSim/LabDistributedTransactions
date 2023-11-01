package be.ucll.da.appointmentservice.domain.appointment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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
    private Long doctorId;
    private Long roomId;
    private Long AccountId;

    public Appointment() {
    }

    public Appointment(String patientFirstName, String patientLastName, String patientNeededExpertise, LocalDate patientPreferredDay) {
        setPatientFirstName(patientFirstName);
        setPatientLastName(patientLastName);
        setPatientNeededExpertise(patientNeededExpertise);
        setPatientPreferredDay(patientPreferredDay);
        setAppointmentStatus(AppointmentStatus.CREATED);
    }

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

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctor) {
        this.doctorId = doctor;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getAccountId() {
        return AccountId;
    }

    public void setAccountId(Long accountId) {
        AccountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!patientFirstName.equals(that.patientFirstName)) return false;
        if (!patientLastName.equals(that.patientLastName)) return false;
        if (!patientNeededExpertise.equals(that.patientNeededExpertise)) return false;
        return patientPreferredDay.equals(that.patientPreferredDay);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + patientFirstName.hashCode();
        result = 31 * result + patientLastName.hashCode();
        result = 31 * result + patientNeededExpertise.hashCode();
        result = 31 * result + patientPreferredDay.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", patientNeededExpertise='" + patientNeededExpertise + '\'' +
                ", patientPreferredDay=" + patientPreferredDay +
                ", appointmentStatus=" + appointmentStatus +
                ", doctorId=" + doctorId +
                '}';
    }
}
