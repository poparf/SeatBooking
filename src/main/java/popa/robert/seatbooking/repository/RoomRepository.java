package popa.robert.seatbooking.repository;

import popa.robert.seatbooking.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    Optional<Room> findByName(String name);
    boolean existsRoomByName(String name);
}
