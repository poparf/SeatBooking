package popa.robert.seatbooking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ROOM")
public class Room {
    @Id
    @Column(name = "NAME", unique = true)
    private String name;
    @Column(name = "MAX_SEATS")
    private Integer maxSeats;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
}
