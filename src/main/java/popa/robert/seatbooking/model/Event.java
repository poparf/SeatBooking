package popa.robert.seatbooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer eventId;

    private Timestamp startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_name", referencedColumnName = "name")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_title", referencedColumnName = "title")
    private Movie movie;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
}
