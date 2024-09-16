package popa.robert.seatbooking.model;

import popa.robert.seatbooking.model.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="SEAT", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"room_name", "seat_number"})})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_name", referencedColumnName = "name")
    private Room room;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name="seat_number")
    private Integer seatNumber;

    @Column(name="MODIFIED_TIMESTAMP")
    private Instant modifiedTimestamp;
}