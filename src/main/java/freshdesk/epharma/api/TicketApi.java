package freshdesk.epharma.api;

import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketAttachment;
import freshdesk.epharma.model.TicketQueryDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    @GetMapping("/tickets/paginated")
    ResponseEntity<List<Ticket>> getTicketsWithPagination(@RequestParam int page);

    @PostMapping("/tickets")
    ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket);

    @PostMapping(value = "/tickets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Ticket> createTicketWithAttachment(
            @RequestPart("ticket") Ticket ticket,
            @RequestPart("attachment") TicketAttachment attachment);

    @PutMapping("/tickets/{id}")
    ResponseEntity<Ticket> updateTicket(@PathVariable (value = "id") Long ticketId,
                                        @RequestBody Ticket ticketDetails) throws ResourceNotFoundException;

    @GetMapping(path = "/search/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    Ticket searchTickets(@ModelAttribute TicketQueryDTO query);

    @DeleteMapping("/tickets/{id}")
    ResponseEntity<String> deleteTicket(@PathVariable(value = "id") Long ticketId) throws ResourceNotFoundException;

    @PutMapping("/tickets/{id}/restore")
    ResponseEntity<String> restoreDeletedTicket(@PathVariable Long id) throws ResourceNotFoundException;

    @PostMapping("/tickets/bulk_delete")
    ResponseEntity<String> deleteTicketsInBulk(@RequestBody Map<String, List<Long>> bulkAction);

}
