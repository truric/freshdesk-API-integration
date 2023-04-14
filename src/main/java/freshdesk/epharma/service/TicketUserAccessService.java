package freshdesk.epharma.service;

import freshdesk.epharma.api.TicketUserAccessApi;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccess;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccessPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TicketUserAccessService implements TicketUserAccessApi {

    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<List<TicketUserAccess>> getTicketUserAccess(Long ticketId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<TicketUserAccess> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketUserAccess[]> responseEntity = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/accesses",
                HttpMethod.GET,
                requestEntity,
                TicketUserAccess[].class);

        List<TicketUserAccess> ticketUserAccessList = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        return new ResponseEntity<>(ticketUserAccessList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TicketUserAccess> createTicketUserAccess(Long ticketId, Long userIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<>(userIds, headers);

        return restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/accesses",
                HttpMethod.POST,
                requestEntity,
                TicketUserAccess.class);
    }

    @Override
    public ResponseEntity<TicketUserAccessPatch> updateTicketUserAccess(Long ticketId, TicketUserAccessPatch ticketUserAccessPatchDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketUserAccessPatch> requestEntity = new HttpEntity<>(ticketUserAccessPatchDetails, headers);

        return restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/accesses",
                HttpMethod.POST,
                requestEntity,
                TicketUserAccessPatch.class);
    }

    @Override
    public ResponseEntity<TicketUserAccess> deleteTicketUserAccess(Long ticketId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketUserAccess> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketUserAccess> response = restTemplate.exchange(
                MAIN_URL + "tickets/" + ticketId + "/accesses",
                HttpMethod.DELETE,
                requestEntity,
                TicketUserAccess.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body(response.getBody());
        } else {
            throw new ResourceNotFoundException("Ticket not deleted");
        }
    }
}
