package popa.robert.seatbooking.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

@Getter
@Setter
public class MovieDTO {
    private String title;
    private String description;
    private String playTime;
    private MultipartFile posterImage;
}
