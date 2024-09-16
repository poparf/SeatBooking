package popa.robert.seatbooking.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import popa.robert.seatbooking.DTO.MovieDTO;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.service.MovieService;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// hasAuthority("ROLE_ADMIN")
@RestController
@RequestMapping("/api/movies")
@CrossOrigin("http://localhost:7070") // client react app
public class MovieController {

    /*
    An admin should be able to CRUD events,rooms,movies and seats
    Read tickets.

    What if a ticket is tied to a specific room/movie/seat and these are deleted by the admin ?
     */

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/test")
    public ResponseEntity<Void> testMapping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createMovie(@ModelAttribute MovieDTO movieDTO) {
        try {
            Movie savedMovie = movieService.createMovie(movieDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedMovie.getTitle())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<Map<String, Object>> getMovies(@RequestParam(required = false) String title,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {

        var res = movieService.createMoviePage(title, PageRequest.of(page, size));
        return ResponseEntity.ok(res);
    }
}
