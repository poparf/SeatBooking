package popa.robert.seatbooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.DTO.RoomDTO;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.exceptions.UniqueConstraintException;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.repository.RoomRepository;
import popa.robert.seatbooking.service.MovieService;
import popa.robert.seatbooking.service.RoomService;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoomService roomService;

    public RoomController(RoomRepository roomRepository, RoomService roomService) {
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Room> getRoom(@PathVariable String name) {
        Optional<Room> room = roomRepository.findByName(name);

        if(room.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room r = room.get();
        if(r.isDeleted()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(r);
    }


    // Returns 409 Conflict in case of a name that is already used.
    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody RoomDTO room) {

        try {
            Room createdRoom = roomService.createRoom(room);
        } catch (UniqueConstraintException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RangeConstraintException e) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{name}")
                    .buildAndExpand(room.getName())
                    .toUri();

        return ResponseEntity.created(location).build();
    }


}
