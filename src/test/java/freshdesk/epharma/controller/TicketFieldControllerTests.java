package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.service.TicketFieldService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TicketFieldControllerTests {
    @Autowired
    private TicketFieldService ticketFieldService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketFormController.class);
    private final Map<String, Object> newTicketFieldCustomText = TestDataFactory.createNewTicketFieldCustomText();
    private final Map<String, Object> newTicketFieldCustomDropDown = TestDataFactory.createNewTicketFieldCustomDropdown();

    @Test
    @DisplayName("Create new Ticket Fields with custom dropdown")
    @Disabled
    void testCreateTicketFieldsWithCustomDropdown() throws JsonProcessingException {
        ResponseEntity<TicketField> response = ticketFieldService.createTicketFields(newTicketFieldCustomDropDown);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketField createdTicketField = response.getBody();
            assertNotNull(createdTicketField);
            assertEquals("custom_dropdown", createdTicketField.getType());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            LOGGER.info(objectMapper.writeValueAsString(createdTicketField));
        } else {
            LOGGER.error("Failed to create Ticket Fields");
        }
    }

    @Test
    @DisplayName("Create new Ticket Fields with a custom text")
    void testCreateTicketFieldsWithCustomText() throws JsonProcessingException {
        ResponseEntity<TicketField> response = ticketFieldService.createTicketFields(newTicketFieldCustomText);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketField createdTicketField = response.getBody();
            assertNotNull(createdTicketField);
            assertEquals("custom_text", createdTicketField.getType());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            LOGGER.info(objectMapper.writeValueAsString(createdTicketField));
        } else {
            LOGGER.error("Failed to create Ticket Fields");
        }
    }
}