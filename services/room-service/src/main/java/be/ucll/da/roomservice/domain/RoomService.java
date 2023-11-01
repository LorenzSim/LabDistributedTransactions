package be.ucll.da.roomservice.domain;

import be.ucll.da.roomservice.api.messaging.model.ReserveRoomCommand;
import be.ucll.da.roomservice.api.messaging.model.RoomReservedEvent;
import be.ucll.da.roomservice.messaging.RabbitMQMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class RoomService {

    private final RabbitMQMessageSender messageSender;

    @Autowired
    public RoomService(RabbitMQMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    // Returns roomID is reservation is successful, -1 otherwise
    public Integer reserveRoom(LocalDate day) {
        var roomReservations = getRoomReservations(day);

        for (var roomReservation : roomReservations.entrySet()) {
            if (!roomReservation.getValue()) { // isReserved -> false, so room is available
                roomReservations.put(roomReservation.getKey(), true);
                return roomReservation.getKey();
            }
        }

        return -1;
    }

    // ---- Internal Code to Simulate Rooms in a Clinic ----

    // There are 3 rooms in the clinic, every room is booked or unbooked for a full day

    // Day -> RoomID -> IsReserved
    private Map<LocalDate, Map<Integer, Boolean>> clinicReservations;

    private Map<Integer, Boolean> getRoomReservations(LocalDate day) {
        if (clinicReservations == null) {
            clinicReservations = new HashMap<>();
        }

        clinicReservations.computeIfAbsent(day, k -> fillInUnreservedRooms());
        return clinicReservations.get(day);
    }

    private Map<Integer, Boolean> fillInUnreservedRooms() {
        var newRoomMap = new HashMap<Integer, Boolean>();
        newRoomMap.put(1, false);
        newRoomMap.put(2, false);
        newRoomMap.put(3, false);

        return newRoomMap;
    }

    public void handleReserveRoomCommand(ReserveRoomCommand command) {
        Integer roomId = reserveRoom(command.getDay());

        RoomReservedEvent event = new RoomReservedEvent()
                .appointmentId(command.getAppointmentId())
                .day(command.getDay())
                .id(roomId);

        messageSender.sendRoomReservedEvent(event);
    }
}
