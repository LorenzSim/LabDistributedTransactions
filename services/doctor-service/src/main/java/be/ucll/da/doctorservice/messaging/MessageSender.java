package be.ucll.da.doctorservice.messaging;

import be.ucll.da.doctorservice.api.messaging.model.DoctorsOnPayrollEvent;

public interface MessageSender {
    void sendDoctorsOnPayrollEvent(DoctorsOnPayrollEvent event);
}
