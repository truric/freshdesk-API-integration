package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketFields.TicketFields;
import freshdesk.epharma.model.TicketForm.TicketForm;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface TicketFormApi {
    @GetMapping("/ticket-forms")
    ResponseEntity<List<TicketForm>> getAllTicketForms();

    @GetMapping("/ticket-forms/{id}")
    ResponseEntity<TicketForm> getTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException;

    @PostMapping("/ticket-forms")
    ResponseEntity<TicketForm> createTicketForm(@RequestBody TicketForm ticketForm);

    @PutMapping("/ticket-forms/{id}")
    ResponseEntity<TicketForm> updateTicketForm(
            @PathVariable (value = "id") Long ticketFormId,
            @RequestBody TicketForm ticketFormDetails) throws ResourceNotFoundException;

    @DeleteMapping("/ticket-forms/{id}")
    ResponseEntity<String> deleteTicketForm(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException;


}
