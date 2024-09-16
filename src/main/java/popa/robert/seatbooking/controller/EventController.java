package popa.robert.seatbooking.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.DTO.EventDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.model.Event;
import popa.robert.seatbooking.model.Ticket;
import popa.robert.seatbooking.repository.EventRepository;
import popa.robert.seatbooking.service.EventService;
import popa.robert.seatbooking.service.TicketService;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/events")
@CrossOrigin("http://localhost:7070")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;
    private final TicketService ticketService;

    public EventController(EventRepository eventRepository, EventService eventService, TicketService ticketService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    // event.getStartTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME)
    // /api/events/roomA?startingTime=2024-09-16T14:30:00
    // ISO is human-readable
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody EventDTO eventDTO) {

        try {
            Event event = eventService.createEvent(eventDTO);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{room}?startingTime=" + event.getStartingTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
                    .buildAndExpand(event.getRoom().getName())
                    .toUri();

            return ResponseEntity.created(location).build();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RangeConstraintException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<Event> getEvent(
            @PathVariable String roomName,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startingTime) {

        Optional<Event> event = eventRepository.findEventByRoomNameAndStartingTime(
                roomName,
                Timestamp.valueOf(startingTime));

        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{eventId}/tickets")
    public ResponseEntity<List<Ticket>> getAllTicketsFromAnEvent(
            @PathVariable Integer eventId) {

        try {
            List<Ticket> tickets = ticketService.getAllTicketsFromAnEvent(eventId);
            if(tickets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tickets);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
