package freshdesk.epharma.api;

import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.Ticket.TicketAttachment;
import freshdesk.epharma.model.Ticket.TicketBulkUpdate;
import freshdesk.epharma.model.Ticket.TicketQueryDTO;
import freshdesk.epharma.model.TicketSummary.TicketSummary;
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

    @GetMapping("/tickets/{id}/summary")
    ResponseEntity<TicketSummary> getTicketSummary(Long ticketId) throws ResourceNotFoundException;

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

    @PutMapping("/tickets/{id}/summary")
    ResponseEntity<TicketSummary> updateTicketsSummary(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody TicketSummary ticketSummaryDetails);

    @PostMapping("/bulk-update")
    ResponseEntity<String> bulkUpdateTickets(@RequestBody TicketBulkUpdate bulkAction);

    @GetMapping(path = "/search/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    Ticket searchTickets(@ModelAttribute TicketQueryDTO query);

    @DeleteMapping("/tickets/{id}")
    ResponseEntity<String> deleteTicket(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException;

    @DeleteMapping("/tickets/{id}/summary")
    ResponseEntity<Ticket> deleteTicketsSummary(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException;

    @DeleteMapping("/attachments/{id}")
    ResponseEntity<TicketAttachment> deleteAnAttachment(@PathVariable(value = "id") Long attachmentId) throws ResourceNotFoundException;

    @DeleteMapping("/archived/{id}")
    ResponseEntity<String> deleteArchivedTicket(@PathVariable(value = "id") Long archivedTicketId);

    @PutMapping("/tickets/{id}/restore")
    ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException;

    @PostMapping("/tickets/bulk_delete")
    ResponseEntity<String> deleteTicketsInBulk(@RequestBody Map<String, List<Long>> bulkAction);

}
