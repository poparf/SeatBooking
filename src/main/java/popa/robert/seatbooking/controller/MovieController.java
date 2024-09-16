package popa.robert.seatbooking.controller;

import org.springframework.data.domain.PageRequest;
import popa.robert.seatbooking.DTO.MovieDTO;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.repository.EventRepository;
import popa.robert.seatbooking.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import popa.robert.seatbooking.service.MovieService;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
    private final MovieRepository movieRepository;
    private final EventRepository eventRepository;

    public MovieController(MovieService movieService, MovieRepository movieRepository, EventRepository eventRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.eventRepository = eventRepository;
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

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMovies(@RequestParam(required = false) String title,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {

        var res = movieService.createMoviePage(title, PageRequest.of(page, size));
        return ResponseEntity.ok(res);
    }

    // What will happen with the events associated with this movie ?
    // Let s do a soft delete by setting a boolean deleted value to true
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String title) {
        Optional<Movie> movie = movieRepository.findByTitleAndDeleted(title, false);

        if(movie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Movie foundMovie = movie.get();
        int updatedCount = movieRepository.setMovieDeleted(foundMovie);

        if(updatedCount == 0) {
            return ResponseEntity.internalServerError().build();
        }

        // Update deleted to all events attributed to this movie
        eventRepository.setEventDeletedByMovie(foundMovie);
        return ResponseEntity.noContent().build();
    }
}
