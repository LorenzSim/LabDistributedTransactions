package be.ucll.da.doctorservice.messaging;

import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;

public interface MessageSender {
    void sendDoctorsEmployedEvent(DoctorsEmployedEvent event);
}
