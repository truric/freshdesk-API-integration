package freshdesk.epharma.service;

import freshdesk.epharma.api.TicketWatcherApi;
import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TicketWatcherService implements TicketWatcherApi {
    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<List<TicketWatcher>> getAllTicketWatchers(@PathVariable(value = "id") Long ticketWatcherId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketWatcher[]> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketWatcherId + "/watchers",
                HttpMethod.GET,
                requestEntity,
                TicketWatcher[].class);

        List<TicketWatcher> tickets = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TicketWatcher> createTicketWatcher(
            @PathVariable (value = "id") Long ticketId,
            @RequestBody Long ticketWatcherId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketWatcherId + "/watchers",
                HttpMethod.POST,
                requestEntity,
                TicketWatcher.class);
    }

    @Override
    public ResponseEntity<TicketWatcher> removeTicketWatcher(@PathVariable (value = "id") Long ticketId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketWatcher> response = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/unwatch",
                HttpMethod.PUT,
                requestEntity,
                TicketWatcher.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body(response.getBody());
        } else {
            throw new ResourceNotFoundException("Watcher not removed");
        }
    }

    @Override
    public ResponseEntity<TicketWatcher> addTicketWatcherToMultipleTickets(@RequestBody TicketWatcher ticketWatcherDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                MAIN_URL + "tickets/bulk_watch",
                HttpMethod.PUT,
                requestEntity,
                TicketWatcher.class);
    }

    @Override
    public ResponseEntity<TicketWatcher> deleteTicketWatcherFromMultipleTickers(@RequestBody List<Long> ticketWatcherIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                MAIN_URL + "tickets/bulk_unwatch",
                HttpMethod.PUT,
                requestEntity,
                TicketWatcher.class);
    }
}
