package popa.robert.seatbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import popa.robert.seatbooking.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,String> {
    Page<Movie> findAllByTitle(String title, Pageable pageable);
}
