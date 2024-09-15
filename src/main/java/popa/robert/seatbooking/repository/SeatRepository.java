package popa.robert.seatbooking.repository;

import popa.robert.seatbooking.model.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
}
