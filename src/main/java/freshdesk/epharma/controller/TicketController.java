package freshdesk.epharma.controller;

import freshdesk.epharma.api.TicketApi;
import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketAttachment;
import freshdesk.epharma.model.TicketQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class TicketController implements TicketApi {
    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate ticketRestTemplate;

    @Override
    public ResponseEntity<List<Ticket>> getAllTickets() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> responseEntity = ticketRestTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.GET,
                requestEntity,
                Ticket[].class);

        List<Ticket> tickets = Arrays.asList(responseEntity.getBody());

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Ticket> getTicketById(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
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

    @Override
    public ResponseEntity<List<Ticket>> getTicketsWithPagination(@RequestParam int page) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> response = ticketRestTemplate.getForEntity(
                MAIN_URL + "tickets?page=" + page,
                Ticket[].class
        );
        Ticket[] tickets = response.getBody();
        return ResponseEntity.ok(Arrays.asList(tickets));
    }

    @Override
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.POST,
                requestEntity,
                Ticket.class);

        return response;
    }

    @Override
    public ResponseEntity<Ticket> createTicketWithAttachment(
            @RequestPart("ticket") Ticket ticket,
            @RequestPart("attachment") TicketAttachment attachment) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("ticket", ticket);
        body.add("attachments[]", attachment);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.POST,
                requestEntity,
                Ticket.class);

        return response;
    }

    @Override
    public ResponseEntity<Ticket> updateTicket(@PathVariable (value = "id") Long ticketId,
                                               @RequestBody Ticket ticketDetails) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticketDetails, headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId,
                HttpMethod.PUT,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Ticket updatedTicket = response.getBody();
            assert updatedTicket != null;

            Ticket updated = new Ticket(
                    updatedTicket.getPhone(),
                    updatedTicket.getSource(),
                    updatedTicket.getStatus(),
                    updatedTicket.getPriority(),
                    updatedTicket.getSubject(),
                    updatedTicket.getDescription(),
                    updatedTicket.getName()
            );
            return ResponseEntity.ok(updated);
        } else {
            throw new ResourceNotFoundException("Ticket not updated");
        }
    }

    @GetMapping(path = "/search/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public Ticket searchTickets(@ModelAttribute TicketQueryDTO query) {
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> queryParams = buildQueryParams(query);

        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                queryStringBuilder.append(key)
                        .append("=")
                        .append(value)
                        .append("&");
            }
        }

        String queryString = queryStringBuilder.toString();
        if (queryString.endsWith("&")) {
            queryString = queryString.substring(0, queryString.length() - 1);
        }

        URI uri = URI.create(MAIN_URL + "search/tickets?query=" + queryString.replace("=",":"));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> responseEntity = ticketRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Ticket.class);

        return responseEntity.getBody();
    }

    @Override
    public ResponseEntity<String> deleteTicket(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = ticketRestTemplate.exchange(
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

    @Override
    public ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
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

    @Override
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

        ResponseEntity<Void> response = ticketRestTemplate.exchange(
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

    private MultiValueMap<String, String> buildQueryParams(TicketQueryDTO query) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (query.getPriority() != null) {
            queryParams.add("%22priority", String.valueOf(query.getPriority()) + "%22");
        }
        if (query.getStatus() != null) {
            queryParams.add("%22status", String.valueOf(query.getStatus()) + "%22");
        }
        if (query.getType() != null) {
            queryParams.add("%22type", query.getType() + "%22");
        }
        if (query.getTag() != null) {
            queryParams.add("%22tag", query.getTag() + "%22");
        }
        if (query.getDueBy() != null) {
            queryParams.add("%22due_by", query.getDueBy().toString() + "%22");
        }
        if (query.getFrDueBy() != null) {
            queryParams.add("%22fr_due_by", query.getFrDueBy().toString() + "%22");
        }
        if (query.getCreatedAt() != null) {
            queryParams.add("%22created_at", query.getCreatedAt().toString() + "%22");
        }
        if (query.getUpdatedAt() != null) {
            queryParams.add("%22updated_at", query.getUpdatedAt().toString() + "%22");
        }
        if (query.getAgentId() != null) {
            queryParams.add("%22agent_id", String.valueOf(query.getAgentId()) + "%22");
        }
        if (query.getGroupId() != null) {
            queryParams.add("%22group_id", String.valueOf(query.getGroupId()) + "%22");
        }
        return queryParams;
    }

}
