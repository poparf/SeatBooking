package popa.robert.seatbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.DTO.EventDTO;
import popa.robert.seatbooking.model.Event;
import popa.robert.seatbooking.repository.EventRepository;
import popa.robert.seatbooking.service.EventService;

import java.net.URI;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/events")
@CrossOrigin("http://localhost:7070")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;

    public EventController(EventRepository eventRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    // event.getStartTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME)
    // /api/events/roomA?startingTime=2024-09-16T14:30:00
    // ISO is human-readable
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventService.createEvent(eventDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{room}?startingTime=" + event.getStartTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
                .buildAndExpand(event.getRoom().getName())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
