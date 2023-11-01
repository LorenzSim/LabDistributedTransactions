package be.ucll.da.appointmentservice.messaging;

import be.ucll.da.accountingservice.api.messaging.model.PatientAccountCreatedEvent;
import be.ucll.da.accountingservice.api.messaging.model.PatientAccountNotCreatedEvent;
import be.ucll.da.doctorservice.api.messaging.model.DoctorsEmployedEvent;
import be.ucll.da.patientservice.api.messaging.model.PatientValidatedEvent;
import be.ucll.da.roomservice.api.messaging.model.RoomReservedEvent;
import org.jetbrains.annotations.NotNull;

public interface EventListener {
    void patientValidated(@NotNull PatientValidatedEvent event);

    void doctorsEmployed(@NotNull DoctorsEmployedEvent event);
    void roomReserved(@NotNull RoomReservedEvent event);

    void accountCreated(@NotNull PatientAccountCreatedEvent event);

    void accountNotCreated(@NotNull PatientAccountNotCreatedEvent event);
}
