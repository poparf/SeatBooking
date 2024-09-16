package popa.robert.seatbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import popa.robert.seatbooking.model.Event;
import popa.robert.seatbooking.model.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByEvent(Event event);
}
