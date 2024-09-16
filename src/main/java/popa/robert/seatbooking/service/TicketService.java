package popa.robert.seatbooking.service;

import org.springframework.stereotype.Service;
import popa.robert.seatbooking.DTO.TicketDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.model.Event;
import popa.robert.seatbooking.model.Seat;
import popa.robert.seatbooking.model.Ticket;
import popa.robert.seatbooking.model.enums.SeatStatus;
import popa.robert.seatbooking.repository.EventRepository;
import popa.robert.seatbooking.repository.SeatRepository;
import popa.robert.seatbooking.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    public Ticket createTicket(TicketDTO ticketDTO) throws RangeConstraintException, NotFoundException {
        if(ticketDTO.getValue() <= 0) {
            throw new RangeConstraintException("Ticket price must be postive.");
        }

        Optional<Event> eventOptional = eventRepository.findEventByEventId(ticketDTO.getEventId());

        if(eventOptional.isEmpty()) {
            throw new NotFoundException("Event with the specified id does not exist: " + ticketDTO.getEventId());
        }
        Event event = eventOptional.get();

        Optional<Seat> seatOptional = seatRepository.
                findSeatByRoomNameAndSeatNumber(
                        event.getRoom().getName(),
                        ticketDTO.getSeatNumber());

        if(seatOptional.isEmpty()) {
            throw new NotFoundException("Seat with the specified number and associated room does not exist: " + ticketDTO.getSeatNumber());
        }
        Seat seat = seatOptional.get();

        if(seat.getStatus() == SeatStatus.PENDING) {
            seatRepository.updateStatusByRoomAndSeatNumber(seat.getRoom(),seat.getSeatNumber(), SeatStatus.BOOKED);
        }

        Ticket createdTicket = new Ticket();
        createdTicket.setTicketType(ticketDTO.getTicketType());
        createdTicket.setSeat(seat);
        createdTicket.setEvent(event);
        createdTicket.setValue(ticketDTO.getValue());
        return ticketRepository.save(createdTicket);
    }

    public List<Ticket> getAllTicketsFromAnEvent(Integer eventId) throws NotFoundException {
        Optional<Event> eventOptional = eventRepository.findEventByEventId(eventId);

        if(eventOptional.isEmpty()) {
            throw new NotFoundException("Event doesn't exist.");
        }
        Event event = eventOptional.get();

        return ticketRepository.findAllByEvent(event);
    }
}
