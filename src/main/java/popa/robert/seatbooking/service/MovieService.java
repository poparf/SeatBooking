package popa.robert.seatbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import popa.robert.seatbooking.DTO.MovieDTO;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.repository.MovieRepository;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(MovieDTO movieDTO) throws IOException{
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setPlayTime(Duration.parse(movieDTO.getPlayTime()));
        movie.setPosterImage(movieDTO.getPosterImage().getBytes());
        return movie;
    }

    public Map<String, Object> createMoviePage(String title, Pageable pageable) {
        Page<Movie> pageMovies;
        if(title == null) {
            pageMovies = movieRepository.findAll(pageable);
        } else {
            pageMovies = movieRepository.findAllByTitle(title, pageable);
        }
        List<Movie> movies = pageMovies.getContent();
        Map<String, Object> res = new HashMap<>();
        res.put("movies", movies);
        res.put("currentPage", pageMovies.getNumber());
        res.put("totalPages", pageMovies.getTotalPages());
        return res;
    }
}
