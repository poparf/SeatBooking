package popa.robert.seatbooking.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import popa.robert.seatbooking.DTO.SeatDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.model.Seat;
import popa.robert.seatbooking.model.enums.SeatStatus;
import popa.robert.seatbooking.repository.RoomRepository;
import popa.robert.seatbooking.repository.SeatRepository;

import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public SeatService(SeatRepository seatRepository, RoomRepository roomRepository) {
        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
    }

    public Seat createSeat(SeatDTO seatDTO) throws NotFoundException {
        Seat createdSeat = new Seat();
        Optional<Room> room = roomRepository.findByName(seatDTO.getRoomName());

        if(room.isEmpty()) {
          throw new NotFoundException("Room does not exist.");
        }

        Room roomFound = room.get();
        if(seatRepository.existsSeatBySeatNumberAndRoom(seatDTO.getSeatNumber(), roomFound)) {
            throw new NotFoundException("There is already a seat with that number in the specified room.");
        }

        createdSeat.setSeatNumber(seatDTO.getSeatNumber());
        createdSeat.setRoom(roomFound);
        createdSeat.setStatus(SeatStatus.AVAILABLE);
        seatRepository.save(createdSeat);
        return createdSeat;
    }
}
