package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketFields.TicketFields;
import freshdesk.epharma.model.TicketForm.TicketForm;
import freshdesk.epharma.service.TicketFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket-forms")
public class TicketFormController {
    @Autowired
    private TicketFormService ticketFormService;

    @GetMapping("/ticket-forms")
    public ResponseEntity<List<TicketForm>> getAllTicketForms() {
        List<TicketForm> ticketForms = ticketFormService.getAllTicketForms().getBody();
        return ResponseEntity.ok(ticketForms);
    }
    @GetMapping("/ticket-forms/{id}")
    public ResponseEntity<TicketForm> getTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        return ticketFormService.getTicketFormById(ticketFormId);
    }

    @PostMapping("/ticket-forms")
    public ResponseEntity<TicketForm> createTicketForm(@RequestBody TicketForm ticketForm) {
        return ticketFormService.createTicketForm(ticketForm);
    }

    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketFields> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        return ticketFormService.createTicketFields(ticketFieldsMap);
    }

    @PutMapping("/ticket-forms/{id}")
    public ResponseEntity<TicketForm> updateTicketForm(
            @PathVariable (value = "id") Long ticketFormId,
            @RequestBody TicketForm ticketFormDetails) throws ResourceNotFoundException {
        return ticketFormService.updateTicketForm(ticketFormId, ticketFormDetails);
    }

    @DeleteMapping("/ticket-forms/{id}")
    public ResponseEntity<String> deleteTicketForm(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        return ticketFormService.deleteTicketForm(ticketFormId);
    }
}
