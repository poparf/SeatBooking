package popa.robert.seatbooking.controller;

import popa.robert.seatbooking.DTO.MovieDTO;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

// hasAuthority("ROLE_ADMIN")
@RestController
@RequestMapping("/api/movies")
@CrossOrigin("localhost:7070") // client react app
public class MovieController {

    /*
    An admin should be able to CRUD events,rooms,movies and seats
    Read tickets.

    What if a ticket is tied to a specific room/movie/seat and these are deleted by the admin ?
     */

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/test")
    public ResponseEntity<Void> testMapping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createMovie(@ModelAttribute MovieDTO movieDTO) {

        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setPlayTime(Duration.parse(movieDTO.getPlayTime()));

        try {
            movie.setPosterImage(movieDTO.getPosterImage().getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Movie savedMovie = movieRepository.save(movie);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMovie.getTitle())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
