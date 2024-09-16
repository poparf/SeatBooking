package popa.robert.seatbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import popa.robert.seatbooking.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import popa.robert.seatbooking.model.Movie;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    int updateDeletedByMovie(Movie movie, boolean deleted);
}
