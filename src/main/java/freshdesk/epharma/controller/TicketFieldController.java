package freshdesk.epharma.controller;

import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.service.TicketFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class TicketFieldController {
    @Autowired
    private TicketFieldService ticketFieldService;

    @GetMapping("/ticket_fields")
    public ResponseEntity<List<TicketField>> getAllTickets() {
        List<TicketField> tickets = ticketFieldService.getAllTicketFields().getBody();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/admin/ticket_fields/{id}")
    public ResponseEntity<TicketField> getTicketFieldById(@PathVariable Long ticketFieldId) {
        return ticketFieldService.getTicketFieldById(ticketFieldId);
    }
    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketField> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        return ticketFieldService.createTicketFields(ticketFieldsMap);
    }
}
