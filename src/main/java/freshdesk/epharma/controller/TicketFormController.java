package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketForm.TicketForm;
import freshdesk.epharma.service.TicketFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/ticket-forms/{id}/clone")
    public ResponseEntity<TicketForm> cloneTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        return ticketFormService.cloneTicketFormById(ticketFormId);
    }

    @GetMapping("/ticket-forms/{form-id}/fields/{field-id}")
    public ResponseEntity<TicketField> viewTicketFormsField(
            @PathVariable(value = "form-id") Long ticketFormId,
            @PathVariable(value = "field-id") Long ticketFieldId) throws ResourceNotFoundException {
        return ticketFormService.viewTicketFormsField(ticketFormId, ticketFieldId);
    }

    @PostMapping("/ticket-forms")
    public ResponseEntity<TicketForm> createTicketForm(@RequestBody TicketForm ticketForm) {
        return ticketFormService.createTicketForm(ticketForm);
    }

    @PutMapping("/ticket-forms/{id}")
    public ResponseEntity<TicketForm> updateTicketForm(
            @PathVariable (value = "id") Long ticketFormId,
            @RequestBody TicketForm ticketFormDetails) throws ResourceNotFoundException {
        return ticketFormService.updateTicketForm(ticketFormId, ticketFormDetails);
    }

    @PutMapping("/ticket-forms/{form-id}/fields/{field-id}")
    public ResponseEntity<TicketField> updateTicketFormsField(
            @PathVariable (value = "form-id") Long ticketFormId,
            @PathVariable (value = "field-id") Long ticketFieldId,
            @RequestBody TicketField ticketFieldDetails) throws ResourceNotFoundException {
        return ticketFormService.updateTicketFormsField(ticketFormId, ticketFieldId, ticketFieldDetails);
    }

    @DeleteMapping("/ticket-forms/{id}")
    public ResponseEntity<String> deleteTicketForm(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        return ticketFormService.deleteTicketForm(ticketFormId);
    }
}
