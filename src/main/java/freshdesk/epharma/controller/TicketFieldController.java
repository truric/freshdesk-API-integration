package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketFields.TicketFieldSection;
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
        List<TicketField> ticketsFields = ticketFieldService.getAllTicketFields().getBody();
        return ResponseEntity.ok(ticketsFields);
    }

    @GetMapping("/admin/ticket_fields")
    ResponseEntity<List<TicketField>> getAllAdminTicketFields() {
        List<TicketField> ticketsFields = ticketFieldService.getAllAdminTicketFields().getBody();
        return ResponseEntity.ok(ticketsFields);
    }

    @GetMapping("/admin/ticket_fields/{id}/sections")
    ResponseEntity<List<TicketField>> getAllSectionsFromTicketFieldId(Long ticketFieldId) {
        List<TicketField> ticketsFields = ticketFieldService.getAllAdminTicketFields().getBody();
        return ResponseEntity.ok(ticketsFields);
    }

    @GetMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<List<TicketField>> getSpecificSectionDetailsFromTicketField(@PathVariable Long ticketFieldId,
                                                                               @PathVariable Long sectionId) {
        List<TicketField> ticketsFields = ticketFieldService.getSpecificSectionDetailsFromTicketField(
                ticketFieldId, sectionId).getBody();
        return ResponseEntity.ok(ticketsFields);
    }

    @GetMapping("/admin/ticket_fields/{id}")
    public ResponseEntity<TicketField> getTicketFieldById(@PathVariable Long ticketFieldId) {
        return ticketFieldService.getTicketFieldById(ticketFieldId);
    }
    @PostMapping("/admin/ticket_fields/{id}/sections")
    public ResponseEntity<TicketField> createSectionForTicketField(@RequestBody Map<String, Object> ticketFieldsMap) {
        return ticketFieldService.createTicketFields(ticketFieldsMap);
    }

    @PostMapping("/admin/ticket_fields")
    public ResponseEntity<TicketField> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        return ticketFieldService.createTicketFields(ticketFieldsMap);
    }

    @PutMapping("/admin/ticket_fields/{id}")
    public ResponseEntity<TicketField> updateTicketField(
            @PathVariable(value = "id") Long ticketFieldId,
            @RequestBody TicketField ticketFieldDetails) {
        return ticketFieldService.updateTicketField(ticketFieldId, ticketFieldDetails);
    }

    @PutMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<TicketFieldSection> updateSectionFromTicketField(
            @PathVariable(value = "ticketFieldId") Long ticketFieldId,
            @PathVariable(value = "sectionId") Long sectionId,
            @RequestBody TicketFieldSection ticketFieldDetails) {
        return ticketFieldService.updateSectionFromTicketField(ticketFieldId, sectionId, ticketFieldDetails);
    }

    @DeleteMapping("/admin/ticket_fields/{id}")
    public ResponseEntity<String> deleteTicketField(@PathVariable(value = "id") Long ticketFieldId) {
        return ticketFieldService.deleteTicketField(ticketFieldId);
    }

    @DeleteMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<String> deleteSectionFromTicketField(
            @PathVariable(value = "ticketFieldId") Long ticketFieldId,
            @PathVariable(value = "sectionId") Long sectionId) {
        return ticketFieldService.deleteSectionFromTicketField(ticketFieldId, sectionId);
    }
}
