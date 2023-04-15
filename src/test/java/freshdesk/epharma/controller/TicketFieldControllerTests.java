package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketFields.TicketFieldResponse;
import freshdesk.epharma.service.TicketFieldService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketFieldControllerTests {
    @Autowired
    private TicketFieldService ticketFieldService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketFormController.class);
    private final Map<String, Object> newTicketFieldCustomText = TestDataFactory.createNewTicketFieldCustomText();
    private final Map<String, Object> newTicketFieldCustomDropDown = TestDataFactory.createNewTicketFieldCustomDropdown();

    @Test
    @DisplayName("Get Ticket Field list")
    @Order(1)
    void testGetTicketFields() throws JsonProcessingException {
        ResponseEntity<List<TicketFieldResponse>> response = ticketFieldService.getAllTicketFields();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    @DisplayName("Create new Ticket Fields with custom dropdown")
    @Order(1)
    void testCreateTicketFieldsWithCustomDropdown() throws JsonProcessingException {
        ResponseEntity<TicketField> response = ticketFieldService.createTicketFields(newTicketFieldCustomDropDown);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        TicketField createdTicketField = response.getBody();
        assertNotNull(createdTicketField);
        assertEquals("custom_dropdown", createdTicketField.getType());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LOGGER.info(objectMapper.writeValueAsString(createdTicketField));
    }

    @Test
    @DisplayName("Create new Ticket Fields with a custom text")
    @Order(1)
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

//    these TicketField properties cannot be updated:
//    placeholder_for_customers
//    hint_for_customers
//    type
    @Test
    @DisplayName("")
    void testUpdateTicketField() {

    }
}