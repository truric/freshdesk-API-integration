package freshdesk.epharma.api;

import freshdesk.epharma.model.TicketWatcher.MultiTicketWatcherResult;
import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherBulkUnwatchRequest;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TicketWatcherApi {
    @GetMapping("/tickets/{id}/watchers")
    ResponseEntity<List<TicketWatcherResponse>> getAllTicketWatchers(@PathVariable (value = "id") Long ticketId);

    @PostMapping("/tickets/{id}/watchers")
    ResponseEntity<String> createTicketWatcher(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody Long ticketWatcherId);

    @PutMapping("/tickets/{id}/unwatch")
    ResponseEntity<TicketWatcher> removeTicketWatcher(@PathVariable (value = "id") Long ticketId);

    @PutMapping("/tickets/bulk_watch")
    ResponseEntity<MultiTicketWatcherResult> addTicketWatcherToMultipleTickets(@RequestBody TicketWatcher ticketWatcherDetails);

    @PutMapping("/tickets/bulk_unwatch")
    ResponseEntity<MultiTicketWatcherResult> deleteTicketWatcherFromMultipleTickers(@RequestBody TicketWatcherBulkUnwatchRequest request);

}
