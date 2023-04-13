package freshdesk.epharma.controller;

import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.Ticket.TicketAttachment;
import freshdesk.epharma.model.Ticket.TicketBulkUpdate;
import freshdesk.epharma.model.TicketSummary.TicketSummary;
import freshdesk.epharma.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets().getBody();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<TicketSummary> getTicketSummary(@PathVariable Long id) {
        return ticketService.getTicketSummary(id);
    }

    @PostMapping("/tickets")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        return ticketService.updateTicket(id, ticket);
    }

    @PutMapping("/tickets/{id}/summary")
    public ResponseEntity<TicketSummary> updateTicketsSummary(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody TicketSummary ticketSummaryDetails) {
        return ticketService.updateTicketsSummary(ticketId, ticketSummaryDetails);
    }

    @PostMapping("/bulk-update")
    public @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<String> bulkUpdateTickets(@RequestBody TicketBulkUpdate bulkAction) {
        return ticketService.bulkUpdateTickets(bulkAction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tickets/{id}/summary")
    public ResponseEntity<Ticket> deleteTicketsSummary(@PathVariable(value = "id") Long ticketId) {
        return ticketService.deleteTicketsSummary(ticketId);
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<TicketAttachment> deleteAnAttachment(@PathVariable(value = "id") Long attachmentId) {
        return ticketService.deleteAnAttachment(attachmentId);
    }

    @GetMapping("/archived/{id}")
    public ResponseEntity<Ticket> getArchivedTicketById(@PathVariable Long id) {
        return ticketService.getArchivedTicketById(id);
    }

    @GetMapping("/archived/{id}/conversations")
    public ResponseEntity<Ticket> getAllConversationsOfArchivedTicketById(
            @PathVariable(value = "id") Long archivedTicketId) {
        return ticketService.getAllConversationsOfArchivedTicketById(archivedTicketId);

    }

    @DeleteMapping("/archived/{id}")
    public ResponseEntity<String> deleteArchivedTicket(
            @PathVariable(value = "id") Long archivedTicketId) {
        return ticketService.deleteArchivedTicket(archivedTicketId);
    }
}
