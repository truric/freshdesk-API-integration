package freshdesk.epharma.api;

import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketBulkUpdateResponse;
import freshdesk.epharma.model.TicketQueryDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface TicketApi {
    @GetMapping("/tickets")
    ResponseEntity<List<Ticket>> getAllTickets();

    @GetMapping("/tickets/{id}")
    ResponseEntity<Ticket> getTicketById(Long ticketId) throws ResourceNotFoundException;

    @GetMapping("/archived/{id}")
    ResponseEntity<Ticket> getArchivedTicketById(@PathVariable Long id);

    @GetMapping("/archived/{id}/conversations")
    ResponseEntity<Ticket> getAllConversationsOfArchivedTicketById(
            @PathVariable(value = "id") Long archivedTicketId) throws ResourceNotFoundException;

    @GetMapping("/tickets/paginated")
    ResponseEntity<List<Ticket>> getTicketsWithPagination(@RequestParam int page);

    @PostMapping("/tickets")
    ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket);

    @PostMapping(value = "/tickets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Ticket> createTicketWithAttachment(Ticket ticket);

    @PutMapping("/tickets/{id}")
    ResponseEntity<Ticket> updateTicket(@PathVariable (value = "id") Long ticketId,
                                        @RequestBody Ticket ticketDetails) throws ResourceNotFoundException;

    @PostMapping("/bulk-update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<TicketBulkUpdateResponse> bulkUpdateTickets(
            @RequestBody TicketBulkUpdateResponse bulkUpdateRequest);

    @GetMapping(path = "/search/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    Ticket searchTickets(@ModelAttribute TicketQueryDTO query);

    @DeleteMapping("/tickets/{id}")
    ResponseEntity<String> deleteTicket(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException;

    @DeleteMapping("/archived/{id}")
    ResponseEntity<String> deleteArchivedTicket(
            @PathVariable(value = "id") Long archivedTicketId);

    @PutMapping("/tickets/{id}/restore")
    ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException;

    @PostMapping("/tickets/bulk_delete")
    ResponseEntity<String> deleteTicketsInBulk(@RequestBody Map<String, List<Long>> bulkAction);

}
