package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketForm.TicketForm;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TicketFormApi {
    @GetMapping("/ticket-forms")
    ResponseEntity<List<TicketForm>> getAllTicketForms();

    @GetMapping("/ticket-forms/{id}")
    ResponseEntity<TicketForm> getTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException;

    @GetMapping("/ticket-forms/{id}/clone")
    ResponseEntity<TicketForm> cloneTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException;

    @GetMapping("/ticket-forms/{form-id}/fields/{field-id}")
    ResponseEntity<TicketField> viewTicketFormsField(
    @PathVariable(value = "form-id") Long ticketFormId,
    @PathVariable(value = "field-id") Long ticketFieldId) throws ResourceNotFoundException;

    @PostMapping("/ticket-forms")
    ResponseEntity<TicketForm> createTicketForm(@RequestBody TicketForm ticketForm);

    @PutMapping("/ticket-forms/{id}")
    ResponseEntity<TicketForm> updateTicketForm(
    @PathVariable (value = "id") Long ticketFormId,
    @RequestBody TicketForm ticketFormDetails) throws ResourceNotFoundException;

    @PutMapping("/ticket-forms/{form-id}/fields/{field-id}")
    ResponseEntity<TicketField> updateTicketFormsField(
    @PathVariable (value = "form-id") Long ticketFormId,
    @PathVariable (value = "field-id") Long ticketFieldId,
    @RequestBody TicketField ticketFieldDetails) throws ResourceNotFoundException;

    @DeleteMapping("/ticket-forms/{id}")
    ResponseEntity<String> deleteTicketForm(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException;


}
