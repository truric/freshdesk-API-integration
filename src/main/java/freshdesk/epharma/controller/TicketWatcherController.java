package freshdesk.epharma.controller;

import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import freshdesk.epharma.service.TicketWatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketWatcherController {

    @Autowired
    private TicketWatcherService ticketWatcherService;
    @GetMapping("/tickets/{id}/watchers")
    public ResponseEntity<List<TicketWatcher>> getAllTicketWatchers(@PathVariable(value = "id") Long ticketWatcherId) {
        return ticketWatcherService.getAllTicketWatchers(ticketWatcherId);
    }

    @PostMapping("/tickets/{id}/watchers")
    public ResponseEntity<TicketWatcher> createTicketWatcher(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody Long ticketWatcherId) {
        return ticketWatcherService.createTicketWatcher(ticketId, ticketWatcherId);
    }

    @PutMapping("/tickets/{id}/unwatch")
    public ResponseEntity<TicketWatcher> removeTicketWatcher(@PathVariable (value = "id") Long ticketWatcherId) {
        return ticketWatcherService.removeTicketWatcher(ticketWatcherId);
    }

    @PutMapping("/tickets/bulk_watch")
    public ResponseEntity<TicketWatcher> addTicketWatcherToMultipleTickets(@RequestBody TicketWatcher ticketWatcherDetails) {
        return ticketWatcherService.addTicketWatcherToMultipleTickets(ticketWatcherDetails);
    }

    @PutMapping("/tickets/bulk_unwatch")
    public ResponseEntity<TicketWatcher> deleteTicketWatcherFromMultipleTickers(@RequestBody List<Long> ticketWatcherIds) {
        return ticketWatcherService.deleteTicketWatcherFromMultipleTickers(ticketWatcherIds);
    }
}
