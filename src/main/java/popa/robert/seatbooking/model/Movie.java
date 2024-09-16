package popa.robert.seatbooking.model;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.Duration;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOVIE")
public class Movie {

    @Id
    @Column(name="TITLE", nullable = false)
    private String title;

    @Column(name="DESCRIPTION", nullable = false)
    private String description;

    @Type(PostgreSQLIntervalType.class)
    @Column(name="PLAY_TIME", nullable = false, columnDefinition = "interval")
    private Duration playTime;

    @Column(name="POSTER_IMAGE", nullable = false)
    private byte[] posterImage;

    @Column(name="DELETED", nullable = false)
    private boolean deleted;
}
