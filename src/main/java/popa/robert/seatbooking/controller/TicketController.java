package popa.robert.seatbooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.DTO.TicketDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.model.Ticket;
import popa.robert.seatbooking.repository.TicketRepository;
import popa.robert.seatbooking.service.TicketService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin("http://localhost:7070")
public class TicketController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    public TicketController(TicketService ticketService, TicketRepository ticketRepository) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = ticketService.createTicket(ticketDTO);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(ticket.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        } catch (RangeConstraintException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable UUID id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
