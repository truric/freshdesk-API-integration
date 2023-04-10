package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketFields.TicketFields;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface TicketFieldApi {
    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketFields> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap);

}
