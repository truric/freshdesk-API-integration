package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketFields.TicketField;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface TicketFieldApi {

    @GetMapping("/ticket_fields")
    ResponseEntity<List<TicketField>> getAllTicketFields();

    @GetMapping("/admin/ticket_fields/{id}")
    ResponseEntity<TicketField> getTicketFieldById(Long ticketFieldId) throws ResourceNotFoundException;

    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketField> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap);

}
