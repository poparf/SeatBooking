package popa.robert.seatbooking.DTO;

import lombok.Getter;
import lombok.Setter;
import popa.robert.seatbooking.model.enums.TicketType;

@Getter
@Setter
public class TicketDTO {
    private Integer eventId;
    private TicketType ticketType;
    private Float value;
    private Integer seatNumber;
}
