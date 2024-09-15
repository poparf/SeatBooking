package popa.robert.seatbooking.repository;

import popa.robert.seatbooking.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie,String> {
}
