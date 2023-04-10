package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketFields.TicketFieldSection;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface TicketFieldApi {

    @GetMapping("/ticket_fields")
    ResponseEntity<List<TicketField>> getAllTicketFields();

    @GetMapping("/admin/ticket_fields")
    ResponseEntity<List<TicketField>> getAllAdminTicketFields();

    @GetMapping("admin/ticket_fields/{id}/sections")
    ResponseEntity<List<TicketField>> getAllSectionsFromTicketFieldId(Long ticketFieldId) throws ResourceNotFoundException;

    @GetMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<List<TicketField>> getSpecificSectionDetailsFromTicketField(
    @PathVariable(value = "ticketFieldId") Long ticketFieldId,
    @PathVariable(value = "sectionId") Long sectionId);

    @GetMapping("/admin/ticket_fields/{id}")
    ResponseEntity<TicketField> getTicketFieldById(Long ticketFieldId) throws ResourceNotFoundException;

    @PostMapping("/admin/ticket_fields")
    ResponseEntity<TicketField> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap);

    ResponseEntity<TicketField> createSectionForTicketField(
    @RequestBody TicketFieldSection ticketFieldSection, Long ticketFieldId) throws ResourceNotFoundException;

    @PutMapping("/admin/ticket_fields/{id}")
    ResponseEntity<TicketField> updateTicketField(
            @PathVariable(value = "id") Long ticketFieldId,
            @RequestBody TicketField ticketFieldDetails) throws ResourceNotFoundException;

    @PutMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<TicketFieldSection> updateSectionFromTicketField(
            @PathVariable(value = "ticketFieldId") Long ticketFieldId,
            @PathVariable(value = "sectionId") Long sectionId,
            @RequestBody TicketFieldSection ticketFieldDetails) throws ResourceNotFoundException;

    @DeleteMapping("/admin/ticket_fields/{id}")
    ResponseEntity<String> deleteTicketField(@PathVariable(value = "id") Long ticketFieldId) throws ResourceNotFoundException;

    @DeleteMapping("/admin/ticket_fields/{ticketFieldId}/sections/{sectionId}")
    ResponseEntity<String> deleteSectionFromTicketField(
    @PathVariable(value = "ticketFieldId") Long ticketFieldId,
    @PathVariable(value = "sectionId") Long sectionId) throws ResourceNotFoundException;
}
