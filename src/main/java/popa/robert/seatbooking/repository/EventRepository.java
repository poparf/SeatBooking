package popa.robert.seatbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import popa.robert.seatbooking.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {


}
