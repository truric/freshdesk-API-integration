package freshdesk.epharma.service;

import freshdesk.epharma.api.TicketWatcherApi;
import freshdesk.epharma.model.TicketWatcher.MultiTicketWatcherResult;
import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherBulkUnwatchRequest;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.MethodNotAllowedException;

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
    public ResponseEntity<List<TicketWatcherResponse>> getAllTicketWatchers(@PathVariable(value = "id") Long ticketId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketWatcherResponse> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/watchers",
                HttpMethod.GET,
                requestEntity,
                TicketWatcherResponse.class);

        List<TicketWatcherResponse> ticketUserAccessList = List.of(Objects.requireNonNull(responseEntity.getBody()));

        return new ResponseEntity<>(ticketUserAccessList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createTicketWatcher(@PathVariable(value = "id") Long ticketId,
                                                 @RequestBody Long ticketWatcherId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TicketWatcher> responseEntity = restTemplate.exchange(
                    MAIN_URL + "tickets/" + ticketId + "/watchers",
                    HttpMethod.POST,
                    requestEntity,
                    TicketWatcher.class);

            return ResponseEntity.ok(HttpStatus.CREATED.toString());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("POST method is not allowed. It should be one of these method(s): GET");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
            }
        }
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
    public ResponseEntity<MultiTicketWatcherResult> addTicketWatcherToMultipleTickets(@RequestBody TicketWatcher ticketWatcherDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcher> requestEntity = new HttpEntity<>(ticketWatcherDetails, headers);

        ResponseEntity<MultiTicketWatcherResult> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets/bulk_watch",
                HttpMethod.PUT,
                requestEntity,
                MultiTicketWatcherResult.class);
        return ResponseEntity.ok(responseEntity.getBody());
    }

    @Override
    public ResponseEntity<MultiTicketWatcherResult> deleteTicketWatcherFromMultipleTickers(@RequestBody TicketWatcherBulkUnwatchRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketWatcherBulkUnwatchRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<MultiTicketWatcherResult> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets/bulk_unwatch",
                HttpMethod.PUT,
                requestEntity,
                MultiTicketWatcherResult.class);

        return ResponseEntity.ok(responseEntity.getBody());
    }
}
