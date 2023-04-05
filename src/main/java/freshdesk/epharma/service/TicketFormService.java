package freshdesk.epharma.service;

import freshdesk.epharma.api.TicketFormApi;
import freshdesk.epharma.model.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TicketFormService implements TicketFormApi {

    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate tickeFormtRestTemplate;

    @GetMapping("/ticket-forms")
    public ResponseEntity<List<TicketForm>> getAllTicketForms() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketForm[]> responseEntity = tickeFormtRestTemplate.exchange(
                MAIN_URL + "ticket-forms",
                HttpMethod.GET,
                requestEntity,
                TicketForm[].class);

        List<TicketForm> ticketForms = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        return new ResponseEntity<>(ticketForms, HttpStatus.OK);
    }
    @GetMapping("/ticket-forms/{id}")
    public ResponseEntity<TicketForm> getTicketFormById(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketForm> response = tickeFormtRestTemplate.exchange(
                MAIN_URL + "/ticket-forms/" + ticketFormId,
                HttpMethod.GET,
                requestEntity,
                TicketForm.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TicketForm ticketForm = response.getBody();
            return ResponseEntity.ok().body(ticketForm);
        } else {
            throw new ResourceNotFoundException("Ticket with id: #" + ticketFormId + " not found");
        }
    }

    @PostMapping("/ticket-forms")
    public ResponseEntity<TicketForm> createTicketForm(@RequestBody TicketForm ticketForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketForm> requestEntity = new HttpEntity<>(ticketForm, headers);

        return tickeFormtRestTemplate.exchange(
                MAIN_URL + "ticket-forms",
                HttpMethod.POST,
                requestEntity,
                TicketForm.class);
    }

    @PutMapping("/ticket-forms/{id}")
    public ResponseEntity<TicketForm> updateTicketForm(@PathVariable (value = "id")
                                                       Long ticketFormId,
                                                       @RequestBody TicketForm ticketFormDetails) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketForm> requestEntity = new HttpEntity<>(ticketFormDetails, headers);

        ResponseEntity<TicketForm> response = tickeFormtRestTemplate.exchange(
                MAIN_URL + "ticket-forms/" + ticketFormId,
                HttpMethod.PUT,
                requestEntity,
                TicketForm.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TicketForm updatedTicketForm = response.getBody();
            assert updatedTicketForm != null;

            TicketForm updated = new TicketForm(
                    updatedTicketForm.getTitle(),
                    updatedTicketForm.getDescription(),
                    updatedTicketForm.getFields()
            );
            return ResponseEntity.ok(updated);
        } else {
            throw new ResourceNotFoundException("Ticket Form not updated");
        }
    }

    @DeleteMapping("/ticket-forms/{id}")
    public ResponseEntity<String> deleteTicketForm(@PathVariable(value = "id") Long ticketFormId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = tickeFormtRestTemplate.exchange(
                MAIN_URL + "tickets/" + ticketFormId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Ticket [#" + ticketFormId + "] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Ticket not deleted");
        }
    }
}
