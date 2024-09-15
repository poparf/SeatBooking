package popa.robert.seatbooking.repository;

import popa.robert.seatbooking.model.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<Ticket, UUID> {
}
