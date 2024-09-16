package popa.robert.seatbooking.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import popa.robert.seatbooking.model.Seat;
import popa.robert.seatbooking.model.enums.SeatStatus;
import popa.robert.seatbooking.repository.SeatRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class SeatStatusScheduler {

    private final SeatRepository seatRepository;

    public SeatStatusScheduler(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // runs every 60 seconds
    @Scheduled(fixedRate = 60000)
    public void checkPendingSeats() {
        if(seatRepository.countSeatsByStatus(SeatStatus.PENDING) > 0) {
            Instant threshold = Instant.now().minus(5, ChronoUnit.MINUTES);

            List<Seat> pendingSeats = seatRepository.findPendingSeatsSince(threshold);

            for (Seat s :
                    pendingSeats) {
                seatRepository.updateStatusByRoomNameAndSeatNumber(s.getRoom(), s.getSeatNumber(), SeatStatus.AVAILABLE);
            }
        }
    }
}
