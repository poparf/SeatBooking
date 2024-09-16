package popa.robert.seatbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import popa.robert.seatbooking.DTO.RoomDTO;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.exceptions.UniqueConstraintException;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.repository.RoomRepository;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final Integer MAX_SEATS = 10000;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(RoomDTO room) throws UniqueConstraintException, RangeConstraintException {
        Optional<Room> existingRoom = roomRepository.findByName(room.getName());

        if(existingRoom.isPresent()) {
            throw new UniqueConstraintException("Room already exists with specified name: " + room.getName());
        }

        if(room.getMaxSeats() > MAX_SEATS) {
            throw new RangeConstraintException("Maximum number of seats are 10 000. Client provided: " + room.getMaxSeats());
        }
        Room createdRoom = new Room();
        createdRoom.setName(room.getName());
        createdRoom.setMaxSeats(room.getMaxSeats());
        createdRoom.setDeleted(false);
        roomRepository.save(createdRoom);

        return createdRoom;
    }
}
