package freshdesk.epharma.controller;

import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + "/tickets/" + ticketId,
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

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> response = restTemplate.getForEntity(
                MAIN_URL + "tickets?page=" + page,
                Ticket[].class
        );
        Ticket[] tickets = response.getBody();
        return ResponseEntity.ok(Arrays.asList(tickets));
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody Ticket ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

        ResponseEntity<TicketResponse> response = restTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.POST,
                requestEntity,
                TicketResponse.class);

        return response;
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable (value = "id") Long ticketId, @RequestBody Ticket ticketDetails) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticketDetails, headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId,
                HttpMethod.PUT,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Ticket updatedTicket = response.getBody();
            assert updatedTicket != null;
            TicketResponse updatedTicketResponse = new TicketResponse(
                    updatedTicket.getSubject(),
                    updatedTicket.getDescription(),
                    updatedTicket.getName(),
                    updatedTicket.getRequesterId(),
                    updatedTicket.getStatus(),
                    updatedTicket.getPriority()
            );
            return ResponseEntity.ok(updatedTicketResponse);
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
                MAIN_URL + "tickets/" + ticketId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Ticket [#" + ticketId + "] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Ticket not deleted");
        }
    }

    @PutMapping("/tickets/{id}/restore")
    public ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = restTemplate.exchange(
                MAIN_URL + "tickets/" + id + "/restore",
                HttpMethod.PUT,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok("Ticket [#" + id + "] restored successfully");
        } else {
            throw new ResourceNotFoundException("Ticket with id: #" + id + " not found");
        }
    }

    @PostMapping("/tickets/bulk_delete")
    public ResponseEntity<String> deleteTicketsInBulk(@RequestBody Map<String, List<Long>> bulkAction) {
        List<Long> ids = bulkAction.get("ids");
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("No ticket ids provided for bulk delete");
        }

        Map<String, List<Long>> requestMap = new HashMap<>();
        requestMap.put("ids", ids);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<Long>>> requestEntity = new HttpEntity<>(requestMap, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                MAIN_URL + "tickets/bulk_delete",
                HttpMethod.POST,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.ACCEPTED) {
            return ResponseEntity.ok().body("Tickets " + ids + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete tickets " + ids);
        }
    }

}
