package freshdesk.epharma.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.api.TicketFormApi;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
import freshdesk.epharma.model.TicketFields.TicketFields;
import freshdesk.epharma.model.TicketForm.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    private ResponseEntity<TicketFields> createCustomTextFieldFromMap(Map<String, Object> ticketFieldsMap) {
        Boolean customersCanEdit = (Boolean) ticketFieldsMap.get("customers_can_edit");
        boolean canEdit = customersCanEdit != null && customersCanEdit;

        Boolean displayedToCustomers = (Boolean) ticketFieldsMap.get("displayed_to_customers");
        boolean displayed = displayedToCustomers != null && displayedToCustomers;

        TicketFields ticketFields = TicketFields.builder()
                .labelForCustomers((String) ticketFieldsMap.get("label_for_customers"))
                .customerCanEdit(canEdit)
                .displayedToCustomers(displayed)
                .label((String) ticketFieldsMap.get("label"))
                .type((String) ticketFieldsMap.get("type"))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketFields);
    }
@Autowired
private ObjectMapper objectMapper;
    private ResponseEntity<TicketFields> createCustomDropdownFieldFromMap(Map<String, Object> ticketFieldsMap) {
        Map<String, Object>[] choicesMapArray = (Map<String, Object>[]) ticketFieldsMap.get("choices");

        Boolean customersCanEdit = (Boolean) ticketFieldsMap.get("customers_can_edit");
        boolean canEdit = customersCanEdit != null && customersCanEdit;

        Boolean displayedToCustomers = (Boolean) ticketFieldsMap.get("displayed_to_customers");
        boolean displayed = displayedToCustomers != null && displayedToCustomers;

        TicketFields ticketFields = new TicketFields();
        ticketFields.setCustomerCanEdit(canEdit);
        ticketFields.setLabelForCustomers((String) ticketFieldsMap.get("label_for_customers"));
        ticketFields.setDisplayedToCustomers(displayed);
        ticketFields.setLabel((String) ticketFieldsMap.get("label"));
        ticketFields.setPosition((Integer) ticketFieldsMap.get("position"));
        ticketFields.setType((String) ticketFieldsMap.get("type"));

        List<TicketFieldChoices> choices = new ArrayList<>();

        for (Map<String, Object> choiceMap : choicesMapArray) {
            String value = (String) choiceMap.get("value");
            Integer position = 0;

            if(choiceMap.get("position") instanceof Integer p){
                position = p;
            }

            choices.add(new TicketFieldChoices(value, position));
        }
        ticketFields.setChoices(choices);

        System.out.println(choices.get(0));

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketFields);
    }

    @PostMapping("/admin/ticket_fields")
    public ResponseEntity<TicketFields> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        String type = ticketFieldsMap.get("type").toString();

        TicketFields ticketFields;
        switch (type) {
            case "custom_text":
                ticketFields = createCustomTextFieldFromMap(ticketFieldsMap).getBody();
                break;
            case "custom_dropdown":
                ticketFields = createCustomDropdownFieldFromMap(ticketFieldsMap).getBody();
                break;
            default:
                throw new IllegalArgumentException("Invalid ticket field type: " + type);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = "";
        try {
             payload = objectMapper.writeValueAsString(ticketFields);
            System.out.println(" payload -> " + payload);
        } catch (Exception e){
            e.printStackTrace();
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        return tickeFormtRestTemplate.exchange(
                MAIN_URL + "admin/ticket_fields",
                HttpMethod.POST,
                requestEntity,
                TicketFields.class);
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
