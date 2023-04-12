package freshdesk.epharma.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.api.TicketFieldApi;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketFields.TicketFieldSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
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

        ResponseEntity<TicketField[]> response = restTemplate.exchange(
                MAIN_URL + "ticket_fields",
                HttpMethod.GET,
                requestEntity,
                TicketField[].class);

        List<TicketField> tickets = Arrays.asList(Objects.requireNonNull(response.getBody()));

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TicketField>> getAllAdminTicketFields() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketField[]> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields",
                HttpMethod.GET,
                requestEntity,
                TicketField[].class);

        List<TicketField> tickets = Arrays.asList(Objects.requireNonNull(response.getBody()));

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TicketField>> getAllSectionsFromTicketFieldId(Long ticketFieldId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketField[]> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId + "/sections",
                HttpMethod.GET,
                requestEntity,
                TicketField[].class);

        List<TicketField> tickets = Arrays.asList(Objects.requireNonNull(response.getBody()));

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TicketField>> getSpecificSectionDetailsFromTicketField(Long ticketFieldId, Long sectionId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TicketField[]> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId + "/sections/" + sectionId,
                HttpMethod.GET,
                requestEntity,
                TicketField[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            List<TicketField> ticketField = Arrays.asList(Objects.requireNonNull(response.getBody()));
            return ResponseEntity.ok().body(ticketField);
        } else {
            throw new ResourceNotFoundException("Ticket field with id: #" + ticketFieldId + " not found");
        }
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

    @Override
    public ResponseEntity<TicketField> createSectionForTicketField(
            @RequestBody TicketFieldSection ticketFieldSection, Long ticketFieldId) throws ResourceNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketFieldSection> requestEntity = new HttpEntity<>(ticketFieldSection, headers);

        return restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId + "/sections",
                HttpMethod.POST,
                requestEntity,
                TicketField.class);
    }

    @Override
    public ResponseEntity<TicketField> updateTicketField(Long ticketFieldId,
            TicketField ticketFieldDetails) throws ResourceNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketField> requestEntity = new HttpEntity<>(ticketFieldDetails, headers);

        ResponseEntity<TicketField> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId,
                HttpMethod.PUT,
                requestEntity,
                TicketField.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TicketField updatedTicketField = response.getBody();
            assert updatedTicketField != null;

            TicketField updated = new TicketField(
                    updatedTicketField.getLabelForCustomers(),
                    updatedTicketField.getIsCustomersCanEdit(),
                    updatedTicketField.getIsDisplayedToCustomers(),
                    updatedTicketField.getLabel(),
                    updatedTicketField.getIsRequiredForCustomers(),
                    updatedTicketField.getPosition()
            );
            return ResponseEntity.ok(updated);
        } else {
            throw new ResourceNotFoundException("Ticket field not updated");
        }
    }

    @Override
    public ResponseEntity<TicketFieldSection> updateSectionFromTicketField(
            Long ticketFieldId, Long ticketFieldSectionId, TicketFieldSection ticketFieldSection) throws ResourceNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TicketFieldSection> requestEntity = new HttpEntity<>(ticketFieldSection, headers);

        ResponseEntity<TicketFieldSection> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId + "/sections/" + ticketFieldSectionId,
                HttpMethod.PUT,
                requestEntity,
                TicketFieldSection.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TicketFieldSection updatedTicketFieldSection = response.getBody();
            assert updatedTicketFieldSection != null;

            TicketFieldSection updated = new TicketFieldSection(
                    updatedTicketFieldSection.getLabel(),
                    updatedTicketFieldSection.getChoices()
            );
            return ResponseEntity.ok(updated);
        } else {
            throw new ResourceNotFoundException("Ticket field not updated");
        }
    }

    @Override
    public ResponseEntity<String> deleteTicketField(Long ticketFieldId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Ticket field [#" + ticketFieldId + "] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Ticket field not deleted");
        }
    }

    @Override
    public ResponseEntity<String> deleteSectionFromTicketField(Long ticketFieldId, Long sectionId) throws ResourceNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                MAIN_URL + "/admin/ticket_fields/" + ticketFieldId + "/sections/" + sectionId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok().body("Section [#" + sectionId + "] from Ticket field [#" + ticketFieldId +"] deleted successfully");
        } else {
            throw new ResourceNotFoundException("Section not deleted");
        }
    }

    private TicketField createCustomTextFieldFromMap(Map<String, Object> ticketFieldsMap) {
        Boolean customersCanEdit = (Boolean) ticketFieldsMap.get("customers_can_edit");
        boolean canEdit = customersCanEdit != null && customersCanEdit;

        Boolean displayedToCustomers = (Boolean) ticketFieldsMap.get("displayed_to_customers");
        boolean displayed = displayedToCustomers != null && displayedToCustomers;

        TicketField ticketField = TicketField.builder()
                .labelForCustomers((String) ticketFieldsMap.get("label_for_customers"))
                .isCustomersCanEdit(canEdit)
                .isDisplayedToCustomers(displayed)
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
        ticketField.setIsCustomersCanEdit(canEdit);
        ticketField.setLabelForCustomers((String) ticketFieldsMap.get("label_for_customers"));
        ticketField.setIsDisplayedToCustomers(displayed);
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
