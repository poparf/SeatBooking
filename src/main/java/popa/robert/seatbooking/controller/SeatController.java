package popa.robert.seatbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.DTO.SeatDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.model.Seat;
import popa.robert.seatbooking.model.enums.SeatStatus;
import popa.robert.seatbooking.repository.RoomRepository;
import popa.robert.seatbooking.repository.SeatRepository;
import popa.robert.seatbooking.service.SeatService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin("http://localhost:7070")
public class SeatController {

    private SeatRepository seatRepository;
    private SeatService seatService;
    private RoomRepository roomRepository;


    public SeatController(SeatRepository seatRepository, SeatService seatService, RoomRepository roomRepository) {
        this.seatRepository = seatRepository;
        this.seatService = seatService;
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createSeat(@RequestBody SeatDTO seatDTO) {
        try {
            Seat seat = seatService.createSeat(seatDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{room}/{seatNumber}")
                    .buildAndExpand(seat.getRoom().getName(), seat.getSeatNumber())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{roomName}/{seatNumber}")
    public ResponseEntity<SeatStatus> getSeatStatus(@PathVariable String roomName,
                                                    @PathVariable Integer seatNumber) {

        Optional<SeatStatus> seatStatus = seatRepository.findSeatStatusByRoomNameAndSeatNumber(roomName,seatNumber);

        return seatStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<List<Seat>> getSeats(@PathVariable String roomName) {

        List<Seat> seats = seatRepository.findSeatsByRoomName(roomName);
        if(seats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(seats);
    }


    @PatchMapping("/{roomName}/{seatNumber}")
    public ResponseEntity<SeatStatus> updateSeatAvailability(@PathVariable String roomName,
                                                         @PathVariable Integer seatNumber,
                                                         @RequestBody SeatStatus status) {

        Optional<SeatStatus> optionalActualStatus = seatRepository.findSeatStatusByRoomNameAndSeatNumber(roomName,seatNumber);
        if(optionalActualStatus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        SeatStatus actualStatus = optionalActualStatus.get();
        /*
        Possible scenarios:
        Available -> Pending
        Pending -> Available // in case users cancels checkout
        Pending -> Booked // in case users succeeds with checkout
         */
        // Check if it is pending or booked   ( changing to booked is done automatically after checkout so not here)
        // if yes refuse request
        if(actualStatus.equals(SeatStatus.AVAILABLE)) {
            Optional<Room> r = roomRepository.findByName(roomName);
            if(r.isPresent()) {
                seatRepository.updateStatusByRoomNameAndSeatNumber(r.get(), seatNumber, SeatStatus.PENDING);
                return ResponseEntity.ok(SeatStatus.PENDING);
            } else {
                return ResponseEntity.badRequest().body(actualStatus);
            }
        } else {
            return ResponseEntity.badRequest().body(actualStatus);
        }
    }

}
