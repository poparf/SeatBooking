package popa.robert.seatbooking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.model.Seat;
import org.springframework.data.repository.CrudRepository;
import popa.robert.seatbooking.model.enums.SeatStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    boolean existsSeatBySeatNumberAndRoom(Integer seatNumber, Room room);
    Optional<SeatStatus> findSeatStatusByRoomNameAndSeatNumber(String room_name, Integer seatNumber);
    List<Seat> findSeatsByRoomName(String room_name);

    @Query("UPDATE Seat s SET s.status = :seatStatus WHERE s.room = :room and s.seatNumber = :seatNumber")
    int updateStatusByRoomAndSeatNumber(@Param("room") Room room,
                                             @Param("seatNumber") Integer seatNumber,
                                             @Param("seatStatus") SeatStatus status);

    @Query("SELECT s FROM Seat s WHERE s.status = 'PENDING' AND s.modifiedTimestamp < :threshold")
    List<Seat> findPendingSeatsSince(Instant threshold);


    Optional<Seat> findSeatByRoomNameAndSeatNumber(String room_name, Integer seatNumber);

    int countSeatsByStatus(SeatStatus status);
}
