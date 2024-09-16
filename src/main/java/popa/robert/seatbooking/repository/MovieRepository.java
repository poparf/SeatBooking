package popa.robert.seatbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import popa.robert.seatbooking.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,String> {
    Page<Movie> findAllByTitleAndDeleted(String title, boolean isDeleted, Pageable pageable);
    int updateDeletedByTitle(String title, Boolean deleted);
    Optional<Movie> findByTitleAndDeleted(String title, boolean isDeleted);
}
