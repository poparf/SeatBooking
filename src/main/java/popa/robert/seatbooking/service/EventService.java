package popa.robert.seatbooking.service;

import org.springframework.stereotype.Service;
import popa.robert.seatbooking.DTO.EventDTO;
import popa.robert.seatbooking.exceptions.NotFoundException;
import popa.robert.seatbooking.exceptions.RangeConstraintException;
import popa.robert.seatbooking.model.Event;
import popa.robert.seatbooking.model.Movie;
import popa.robert.seatbooking.model.Room;
import popa.robert.seatbooking.repository.EventRepository;
import popa.robert.seatbooking.repository.MovieRepository;
import popa.robert.seatbooking.repository.RoomRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    public EventService(EventRepository eventRepository, RoomRepository roomRepository, MovieRepository movieRepository) {
        this.eventRepository = eventRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
    }

    // TODO-IMPORTANT: TEST THIS FUNCTION
    public Event createEvent(EventDTO eventDTO) throws NotFoundException, RangeConstraintException {
        Event event = new Event();

        Optional<Room> room = roomRepository.findByName(eventDTO.getRoomName());
        if(room.isEmpty()) {
            throw new NotFoundException("Room not found with name: " + eventDTO.getRoomName());
        }
        Room foundRoom = room.get();

        Optional<Movie> movie = movieRepository.findByTitleAndDeleted(eventDTO.getMovieName(), false);
        if(movie.isEmpty()) {
            throw new NotFoundException("Movie not found with name: " + eventDTO.getMovieName());
        }

        Movie foundMovie = movie.get();
        // i need to make sure that there is not other event in that room
        // in that overlaps with the interval
        // Calculate using movie play_time and event start_time
        // When movie starts
        Timestamp s2 = eventDTO.getStartTime();
        Duration movieDuration = foundMovie.getPlayTime();
        // When movie ends
        Timestamp e2 = new Timestamp(s2.getTime() + movieDuration.toMillis());

        /*
        LOGIC:
           S2 < S1
           E2 < S1
           OR
           S2 > E1
           E2 > E1

           for all events in that room where s2 is the new movie starting time and e2 ending time
         */
        List<Event> events = eventRepository.findAllByRoomName(foundRoom.getName());
        // Get only the events that happen today.
        // TODO:This can be optimized using a custom query, instead of selecting all events from a room
        events = events.stream().filter(e ->
                e.getStartingTime().toLocalDateTime().equals(LocalDate.now()))
                .collect(Collectors.toList());
        for (Event e:
             events) {
            Long otherMovieDuration = e.getMovie().getPlayTime().toMillis();
            Timestamp s1 = e.getStartingTime();
            Timestamp e1 = new Timestamp(
                    e.getStartingTime().getTime() + otherMovieDuration);

            if(!(s2.before(s1) && e2.before(s1))
                    || !(s2.after(e1) && e2.after(e1))) {
                //Not  Acceptable time
                throw new RangeConstraintException(
                        "Requested event: " + s2.toString() + " | " + e2.toString() +  " overlaps with another event: " + s1.toString() + " | " + e1.toString());
            }
        }

        event.setRoom(foundRoom);
        event.setMovie(foundMovie);
        event.setDeleted(false);
        event.setStartingTime(s2);
        eventRepository.save(event);
        return event;
    }
}
