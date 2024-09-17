package popa.robert.seatbooking.proxy;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import popa.robert.seatbooking.model.Ticket;

import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/*
Microservice here : https://github.com/poparf/QRCodeGenerator/tree/master
 */


@Component
public class QRCodeProxy {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8081/api/qrcode";

    public BufferedImage getQRCode(Ticket ticket) throws Exception {

        //TODO-IDEA: Maybe there should be a hash to check the validity of the ticket
        // or just check existence in the database. ??
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket id: ");
        sb.append(ticket.getId());
        sb.append("\nTicket type: ");
        sb.append(ticket.getTicketType().toString());
        sb.append("\nRoom: ");
        sb.append(ticket.getEvent().getRoom().getName());
        sb.append("\nMovie: ");
        sb.append(ticket.getEvent().getMovie().getTitle());
        sb.append("\nSeat: ");
        sb.append(ticket.getSeat().getSeatNumber());
        sb.append("\nStarting time: ");
        sb.append(ticket.getEvent().getStartingTime().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));

        String ticketInfo = sb.toString();
        Map<String, String> body = new HashMap<>();
        body.put("contents", ticketInfo);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<BufferedImage> response = restTemplate
                .exchange(baseUrl, HttpMethod.POST,request,BufferedImage.class);

        if(response.getStatusCode().is4xxClientError()) {
            throw new Exception(response.getBody().toString());
        }

        return response.getBody();
        }
    }
}
