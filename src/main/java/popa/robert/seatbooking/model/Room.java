package popa.robert.seatbooking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="ROOM")
public class Room {
    @Id
    @Column(name = "NAME")
    private String name;
    @Column(name = "MAX_SEATS")
    private Integer max_seats;
}
