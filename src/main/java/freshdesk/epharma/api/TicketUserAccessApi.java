package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketUserAccess.TicketUserAccess;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccessPatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TicketUserAccessApi {
    @GetMapping("/tickets/{id}/accesses")
    ResponseEntity<List<TicketUserAccess>> getTicketUserAccess(@PathVariable(value = "id") Long ticketId);

    @PostMapping("/tickets/{id}/accesses")
    ResponseEntity<TicketUserAccess> createTicketUserAccess(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody TicketUserAccess ticketUserAccessDetails);

    @PatchMapping("/tickets/{id}/accesses")
    ResponseEntity<TicketUserAccessPatch> updateTicketUserAccess(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody TicketUserAccessPatch ticketUserAccessPatchDetails);

    @DeleteMapping("/tickets/{id}/accesses")
    ResponseEntity<TicketUserAccess> deleteTicketUserAccess(@PathVariable(value = "id") Long ticketId);
}