package popa.robert.seatbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import popa.robert.seatbooking.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,String> {
    Page<Movie> findAllByTitleAndDeleted(String title, boolean isDeleted, Pageable pageable);
    @Query("UPDATE Movie m SET m.deleted = TRUE WHERE m = :movie")
    int setMovieDeleted(@Param("movie") Movie movie);
    Optional<Movie> findByTitleAndDeleted(String title, boolean isDeleted);
}
