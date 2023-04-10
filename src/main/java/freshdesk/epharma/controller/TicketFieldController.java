package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketFields.TicketFields;
import freshdesk.epharma.service.TicketFieldService;
import freshdesk.epharma.service.TicketFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ticket_fields")
public class TicketFieldController {
    @Autowired
    private TicketFieldService ticketFieldService;
    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketFields> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        return ticketFieldService.createTicketFields(ticketFieldsMap);
    }
}
