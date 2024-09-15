package popa.robert.seatbooking.model;

import popa.robert.seatbooking.model.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SEAT", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"room_name", "seat_number"})})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer seatId;  // Follow camelCase

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_name", referencedColumnName = "name")
    private Room room;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(name="seat_number")
    private Integer seatNumber;
}