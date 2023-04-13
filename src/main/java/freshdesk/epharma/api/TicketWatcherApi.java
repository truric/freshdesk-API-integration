package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TicketWatcherApi {
    @GetMapping("/tickets/{id}/watchers")
    ResponseEntity<List<TicketWatcher>> getAllTicketWatchers(@PathVariable (value = "id") Long ticketId);

    @PostMapping("/tickets/{id}/watchers")
    ResponseEntity<TicketWatcher> createTicketWatcher(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody Long ticketWatcherId);

    @PutMapping("/tickets/{id}/unwatch")
    ResponseEntity<TicketWatcher> removeTicketWatcher(@PathVariable (value = "id") Long ticketId);

    @PutMapping("/tickets/bulk_watch")
    ResponseEntity<TicketWatcher> addTicketWatcherToMultipleTickets(@RequestBody TicketWatcher ticketWatcherDetails);

    @PutMapping("/tickets/bulk_unwatch")
    ResponseEntity<TicketWatcher> deleteTicketWatcherFromMultipleTickers(@RequestBody List<Long> ticketWatcherIds);

}
