package freshdesk.epharma.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.api.TicketFieldApi;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
import freshdesk.epharma.model.TicketFields.TicketFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketFieldService implements TicketFieldApi {

    @Value("${freshdesk.url.main}")
    private String MAIN_URL;
    @Autowired
    private RestTemplate ticketRestTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/admin/ticket_fields")
    public ResponseEntity<TicketFields> createTicketFields(@RequestBody Map<String, Object> ticketFieldsMap) {
        String type = ticketFieldsMap.get("type").toString();

        TicketFields ticketFields;
        switch (type) {
            case "custom_text":
                ticketFields = createCustomTextFieldFromMap(ticketFieldsMap);
                break;
            case "custom_dropdown":
                ticketFields = createCustomDropdownFieldFromMap(ticketFieldsMap);
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

        return ticketRestTemplate.exchange(
                MAIN_URL + "admin/ticket_fields",
                HttpMethod.POST,
                requestEntity,
                TicketFields.class);
    }

    private TicketFields createCustomTextFieldFromMap(Map<String, Object> ticketFieldsMap) {
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

        return ticketFields;
    }

    private TicketFields createCustomDropdownFieldFromMap(Map<String, Object> ticketFieldsMap) {
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

        return ticketFields;
    }
}
