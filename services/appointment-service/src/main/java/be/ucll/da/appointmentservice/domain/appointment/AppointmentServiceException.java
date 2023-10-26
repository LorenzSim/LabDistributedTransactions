package be.ucll.da.appointmentservice.domain.appointment;

public class AppointmentServiceException extends RuntimeException {
    public AppointmentServiceException() {}
    public AppointmentServiceException(String msg) {super(msg);}
}
