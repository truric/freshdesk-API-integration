package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketUserAccess.TicketUserAccess;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccessPatch;
import freshdesk.epharma.service.TicketUserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketUserAccessController {
    @Autowired
    private TicketUserAccessService ticketUserAccessService;

    @GetMapping("/tickets/{id}/accesses")
    public ResponseEntity<List<TicketUserAccess>> getTicketUserAccess(@PathVariable(value = "id") Long ticketId) {
        return ticketUserAccessService.getTicketUserAccess(ticketId);
    }

    @PostMapping("/tickets/{id}/accesses")
    public ResponseEntity<TicketUserAccess> createTicketUserAccess(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody Long userIds) {
        return ticketUserAccessService.createTicketUserAccess(ticketId,userIds);
    }

    @PatchMapping("/tickets/{id}/accesses")
    public ResponseEntity<TicketUserAccessPatch> updateTicketUserAccess(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody TicketUserAccessPatch ticketUserAccessPatchDetails) {
        return ticketUserAccessService.updateTicketUserAccess(ticketId,ticketUserAccessPatchDetails);
    }

    @DeleteMapping("/tickets/{id}/accesses")
    public ResponseEntity<TicketUserAccess> deleteTicketUserAccess(@PathVariable(value = "id") Long ticketId) {
        return ticketUserAccessService.deleteTicketUserAccess(ticketId);
    }
}
