package freshdesk.epharma.controller;

import freshdesk.epharma.error.ResourceNotFoundException;
import freshdesk.epharma.model.Ticket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TicketController {
    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.GET,
                requestEntity,
                Ticket[].class);

        List<Ticket> tickets = Arrays.asList(responseEntity.getBody());

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId,
                HttpMethod.GET,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Ticket ticket = response.getBody();
            return ResponseEntity.ok().body(ticket);
        } else {
            throw new ResourceNotFoundException("Ticket with id: #" + ticketId + " not found");
        }
    }

    @GetMapping("/tickets/paginated")
    public ResponseEntity<List<Ticket>> getTicketsWithPagination(@RequestParam int page) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> response = restTemplate.getForEntity(
                MAIN_URL + "/tickets?page=" + page,
                Ticket[].class
        );
        Ticket[] tickets = response.getBody();
        return ResponseEntity.ok(Arrays.asList(tickets));
    }

    @PostMapping("/tickets")
    public ResponseEntity<String> createTicket(@RequestBody @Valid Ticket ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL,
                HttpMethod.POST,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Ticket [#" + ticket.getId() + "] created successfully");
        } else {
            throw new ResourceNotFoundException("Ticket not created");
        }
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<String> updateTicket(@PathVariable (value = "id") Long ticketId, @RequestBody Ticket ticketDetails) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + "/" + ticketId,
                HttpMethod.PUT,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Ticket [#" + ticketDetails.getId() + "] updated successfully");
        } else {
            throw new ResourceNotFoundException("Ticket not updated");
        }
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                MAIN_URL + "/" + ticketId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Ticket [#" + ticketId + "] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Ticket not deleted");
        }
    }

//    TODO
    @PutMapping("/tickets/{id}/restore")
    public ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + id + "/restore",
                HttpMethod.PUT,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Ticket [#" + id + "] restored successfully");
        } else {
            throw new ResourceNotFoundException("Ticket with id: #" + id + " not found");
        }
    }

}
