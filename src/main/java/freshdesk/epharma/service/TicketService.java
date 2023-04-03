package freshdesk.epharma.service;

import freshdesk.epharma.api.TicketApi;
import freshdesk.epharma.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketService implements TicketApi {

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

        List<Ticket> tickets = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

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
    public ResponseEntity<Ticket> getTicketSummary(Long ticketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "/tickets/" + ticketId + "summary",
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
    public ResponseEntity<Ticket> getArchivedTicketById(
            @PathVariable(value = "id") Long archivedTicketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets/archived/" + archivedTicketId,
                HttpMethod.GET,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Ticket ticket = response.getBody();
            return ResponseEntity.ok().body(ticket);
        } else {
            throw new ResourceNotFoundException("Archived Ticket with id: #" + archivedTicketId + " not found");
        }
    }

    @Override
    public ResponseEntity<Ticket> getAllConversationsOfArchivedTicketById(
            @PathVariable(value = "id") Long archivedTicketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets/archived/" + archivedTicketId + "/conversations",
                HttpMethod.GET,
                requestEntity,
                Ticket.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Ticket ticket = response.getBody();
            return ResponseEntity.ok().body(ticket);
        } else {
            throw new ResourceNotFoundException("Archived Ticket with id: #" + archivedTicketId + " not found");
        }
    }

    @Override
    public ResponseEntity<List<Ticket>> getTicketsWithPagination(@RequestParam int page) {
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Ticket[]> response = ticketRestTemplate.getForEntity(
                MAIN_URL + "tickets?page=" + page,
                Ticket[].class
        );
        Ticket[] tickets = response.getBody();
        assert tickets != null;
        return ResponseEntity.ok(Arrays.asList(tickets));
    }

    @Override
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Ticket> requestEntity = new HttpEntity<>(ticket, headers);

        return ticketRestTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.POST,
                requestEntity,
                Ticket.class);
    }

    @Override
    public ResponseEntity<Ticket> createTicketWithAttachment(Ticket ticket) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        if (ticket.getAttachments().size() > 0) {
            for (int i = 0; i < ticket.getAttachments().size(); i++) {
                int changeNameIterator = i;
                body.add("attachments[]", new ByteArrayResource(ticket.getAttachments().get(changeNameIterator).getData()) {
                    @Override
                    public String getFilename() {
                        return ticket.getAttachments().get(changeNameIterator).getName();
                    }
                });
            }
        }

        body.add("email", ticket.getEmail());
        body.add("subject", ticket.getSubject());
        body.add("description", ticket.getDescription());
        body.add("source", ticket.getSource());
        body.add("priority", ticket.getPriority());
        body.add("status", ticket.getStatus());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return ticketRestTemplate.exchange(
                MAIN_URL + "tickets",
                HttpMethod.POST,
                requestEntity,
                Ticket.class);
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

    @Override
    public ResponseEntity<TicketBulkUpdateResponse> bulkUpdateTickets(
            @RequestBody TicketBulkUpdateResponse bulkUpdateRequest) {

        List<Long> ids = bulkUpdateRequest.getIds();
        Map<String, Ticket> updatedProperties = bulkUpdateRequest.getProperties();
        TicketReply reply = bulkUpdateRequest.getTicketReply();

        URI uri = URI.create(MAIN_URL + "tickets/bulk_update");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBodyMap = buildBulkUpdateRequestBody(updatedProperties, reply);
        requestBodyMap.put("ids", ids);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBodyMap, headers);

        ResponseEntity<TicketBulkUpdateResponse> responseEntity = ticketRestTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                TicketBulkUpdateResponse.class
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseEntity.getBody());
    }

    @Override
    public Ticket searchTickets(@ModelAttribute TicketQueryDTO query) {
        MultiValueMap<String, String> queryParams = buildQueryParams(query);

        String queryString = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + entry.getValue().get(0))
                .collect(Collectors.joining(" "));

        URI uri = URI.create(MAIN_URL + "search/tickets?query=" + queryString);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Ticket> responseEntity = ticketRestTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Ticket.class
        );

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
    public ResponseEntity<String> deleteArchivedTicket(
            @PathVariable(value = "id") Long archivedTicketId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = ticketRestTemplate.exchange(
                MAIN_URL + "tickets/archived/" + archivedTicketId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Archived ticket [#" + archivedTicketId + "] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Archived ticket not deleted");
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

        if (query.getAgentId() != null) {
            queryParams.add("%22", "agent_id:" + query.getAgentId() + "%22");
        }
        if (query.getGroupId() != null) {
            queryParams.add("%22", "group_id:" + query.getGroupId() + "%22");
        }
        if (query.getPriority() != null) {
            queryParams.add("%22", "priority:" + query.getPriority() + "%22");
        }
        if (query.getStatus() != null) {
            queryParams.add("%22", "status:" + query.getStatus() + "%22");
        }
        if (query.getTag() != null) {
            queryParams.add("%22", "tag:" + query.getTag() + "%22");
        }
        if (query.getType() != null) {
            queryParams.add("%22", "type:" + query.getType() + "%22");
        }
        if (query.getDueBy() != null) {
            queryParams.add("%22", "due_by:" + query.getDueBy() + "%22");
        }
        if (query.getFrDueBy() != null) {
            queryParams.add("%22", "fr_due_by:" + query.getFrDueBy() + "%22");
        }
        if (query.getCreatedAt() != null) {
            queryParams.add("%22", "created_at:" + query.getCreatedAt() + "%22");
        }
        if (query.getUpdatedAt() != null) {
            queryParams.add("%22", "updated_at:" + query.getUpdatedAt() + "%22");
        }

        return queryParams;
    }

    private Map<String, Object> buildBulkUpdateRequestBody(Map<String, Ticket> updatedProperties, TicketReply reply) {
        Map<String, Object> requestBodyMap = new HashMap<>();

        if (updatedProperties != null && !updatedProperties.isEmpty()) {
            Map<String, Object> propertiesMap = new HashMap<>();
            for (Map.Entry<String, Ticket> entry : updatedProperties.entrySet()) {
                String property = entry.getKey();
                Ticket value = entry.getValue();
                Map<String, Object> valueMap = new HashMap<>();

                switch (property) {
                    case "phone" -> valueMap.put("phone", value.getPhone());
                    case "source" -> valueMap.put("source", value.getSource());
                    case "status" -> valueMap.put("status", value.getStatus());
                    case "priority" -> valueMap.put("priority", value.getPriority());
                    case "subject" -> valueMap.put("subject", value.getSubject());
                    case "description" -> valueMap.put("description", value.getDescription());
                    case "name" -> valueMap.put("name", value.getName());
                    case "requesterId" -> valueMap.put("requester_id", value.getRequesterId());
                    case "attachments" -> valueMap.put("attachments", value.getAttachments());
                    default -> throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                            "Invalid property name: " + property);
                }

                propertiesMap.put(property, valueMap);
            }
            requestBodyMap.put("properties", propertiesMap);
        }

        if (reply != null) {
            Map<String, Object> replyMap = new HashMap<>();
            replyMap.put("body", reply.getBody());
            requestBodyMap.put("reply", replyMap);
        }

        return requestBodyMap;
    }
}
