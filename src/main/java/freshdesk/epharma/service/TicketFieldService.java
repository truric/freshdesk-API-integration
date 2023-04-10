package freshdesk.epharma.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.api.TicketFieldApi;
import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
import freshdesk.epharma.model.TicketFields.TicketField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class TicketFieldService implements TicketFieldApi {

    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public ResponseEntity<List<TicketField>> getAllTicketFields() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketField[]> responseEntity = restTemplate.exchange(
                MAIN_URL + "ticket_fields",
                HttpMethod.GET,
                requestEntity,
                TicketField[].class);

        List<TicketField> tickets = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TicketField> getTicketFieldById(Long ticketFieldId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketField> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId,
                HttpMethod.GET,
                requestEntity,
                TicketField.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TicketField ticketField = response.getBody();
            return ResponseEntity.ok().body(ticketField);
        } else {
            throw new ResourceNotFoundException("Ticket field with id: #" + ticketFieldId + " not found");
        }
    }

    @PostMapping("/admin/ticket_fields")
    public ResponseEntity<TicketField> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        String type = ticketFieldsMap.get("type").toString();

        TicketField ticketField;
        switch (type) {
            case "custom_text":
                ticketField = createCustomTextFieldFromMap(ticketFieldsMap);
                break;
            case "custom_dropdown":
                ticketField = createCustomDropdownFieldFromMap(ticketFieldsMap);
                break;
            default:
                throw new IllegalArgumentException("Invalid ticket field type: " + type);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(ticketField);
            System.out.println(" payload -> " + payload);
        } catch (Exception e){
            e.printStackTrace();
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        return restTemplate.exchange(
                MAIN_URL + "admin/ticket_fields",
                HttpMethod.POST,
                requestEntity,
                TicketField.class);
    }

    private TicketField createCustomTextFieldFromMap(Map<String, Object> ticketFieldsMap) {
        Boolean customersCanEdit = (Boolean) ticketFieldsMap.get("customers_can_edit");
        boolean canEdit = customersCanEdit != null && customersCanEdit;

        Boolean displayedToCustomers = (Boolean) ticketFieldsMap.get("displayed_to_customers");
        boolean displayed = displayedToCustomers != null && displayedToCustomers;

        TicketField ticketField = TicketField.builder()
                .labelForCustomers((String) ticketFieldsMap.get("label_for_customers"))
                .customerCanEdit(canEdit)
                .displayedToCustomers(displayed)
                .label((String) ticketFieldsMap.get("label"))
                .type((String) ticketFieldsMap.get("type"))
                .build();

        return ticketField;
    }

    private TicketField createCustomDropdownFieldFromMap(Map<String, Object> ticketFieldsMap) {
        Map<String, Object>[] choicesMapArray = (Map<String, Object>[]) ticketFieldsMap.get("choices");

        Boolean customersCanEdit = (Boolean) ticketFieldsMap.get("customers_can_edit");
        boolean canEdit = customersCanEdit != null && customersCanEdit;

        Boolean displayedToCustomers = (Boolean) ticketFieldsMap.get("displayed_to_customers");
        boolean displayed = displayedToCustomers != null && displayedToCustomers;

        TicketField ticketField = new TicketField();
        ticketField.setCustomerCanEdit(canEdit);
        ticketField.setLabelForCustomers((String) ticketFieldsMap.get("label_for_customers"));
        ticketField.setDisplayedToCustomers(displayed);
        ticketField.setLabel((String) ticketFieldsMap.get("label"));
        ticketField.setPosition((Integer) ticketFieldsMap.get("position"));
        ticketField.setType((String) ticketFieldsMap.get("type"));

        List<TicketFieldChoices> choices = new ArrayList<>();

        for (Map<String, Object> choiceMap : choicesMapArray) {
            String value = (String) choiceMap.get("value");
            Integer position = 0;

            if(choiceMap.get("position") instanceof Integer p){
                position = p;
            }

            choices.add(new TicketFieldChoices(value, position));
        }
        ticketField.setChoices(choices);

        return ticketField;
    }
}
