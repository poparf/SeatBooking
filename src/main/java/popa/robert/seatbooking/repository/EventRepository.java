package popa.robert.seatbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import popa.robert.seatbooking.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import popa.robert.seatbooking.model.Movie;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("UPDATE Event e SET e.deleted = TRUE WHERE e.movie = :movie")
    int setEventDeletedByMovie(@Param("movie") Movie movie);
    List<Event> findAllByRoomName(String room_name);
    Optional<Event> findEventByRoomNameAndStartingTime(String room_name, Timestamp startingTime);
}
