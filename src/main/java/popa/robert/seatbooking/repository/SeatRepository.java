package popa.robert.seatbooking.repository;

import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.model.Seat;
import org.springframework.data.repository.CrudRepository;
import popa.robert.seatbooking.model.enums.SeatStatus;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    boolean existsSeatBySeatNumberAndRoom(Integer seatNumber, Room room);
    Optional<SeatStatus> findSeatStatusByRoomNameAndSeatNumber(String room_name, Integer seatNumber);
    List<Seat> findSeatsByRoomName(String room_name);
}
