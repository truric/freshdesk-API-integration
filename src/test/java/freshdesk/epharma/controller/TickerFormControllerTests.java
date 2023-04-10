package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketFields.TicketFields;
import freshdesk.epharma.model.TicketForm.TicketForm;
import freshdesk.epharma.service.TicketFormService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TickerFormControllerTests {

    @Autowired
    private TicketFormService ticketFormService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketFormController.class);
//    private final TicketForm newTicketForm = TestDataFactory.createNewTicketForm();
    private final Map<String, Object> newTicketFieldCustomText = TestDataFactory.createNewTicketFieldCustomText();
    private final Map<String, Object> newTicketFieldCustomDropDown = TestDataFactory.createNewTicketFieldCustomDropdown();

//    private final TicketForm updatedTicketForm = TestDataFactory.createUpdatedTicketForm();

    @Test
    @DisplayName("Get all Ticket Forms")
    @Order(1)
    void testGetAllTicketForms() {
        ResponseEntity<List<TicketForm>> responseEntity = ticketFormService.getAllTicketForms();
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            List<TicketForm> ticketForms = responseEntity.getBody();
            assert ticketForms != null;
            for (TicketForm ticketForm : ticketForms) {
                LOGGER.info(ticketForm.toString());
            }

        } else {
            LOGGER.error("Failed to retrieve tickets, status code: {}", statusCode);
        }
    }

    @Test
    @DisplayName("Get a Ticket Form by its id")
    @Disabled
    void testGetTicketFormById() throws JsonProcessingException {
        long ticketFormId = 103000131305L;
        ResponseEntity<TicketForm> response = ticketFormService.getTicketFormById(ticketFormId);
        if (response.getStatusCode() == HttpStatus.OK) {
            TicketForm ticketForm = response.getBody();
            assert ticketForm != null;
            LOGGER.info(objectMapper.writeValueAsString(ticketForm));
        } else {
            LOGGER.error("Unable to retrieve Ticket Form with ID " + ticketFormId + ". HTTP status: " + response.getStatusCode());
        }
    }

//    @Test
//    @DisplayName("Create a new Ticket Form")
//    @Disabled
//    @Order(3)
//    void testCreateTicket() throws JsonProcessingException {
//        ResponseEntity<TicketForm> response = ticketFormService.createTicketForm(newTicketForm);
//        HttpStatusCode httpStatus = response.getStatusCode();
//
//        if (httpStatus == HttpStatus.CREATED) {
//            TicketForm createdTicketForm = response.getBody();
//            assert createdTicketForm != null;
//            LOGGER.info(objectMapper.writeValueAsString(createdTicketForm));
//        } else {
//            LOGGER.error("Failed to create Ticket Form");
//        }
//    }

    @Test
    @DisplayName("Create new Ticket Fields with custom dropdown")
    @Disabled
    void testCreateTicketFieldsWithCustomDropdown() throws JsonProcessingException {
        ResponseEntity<TicketFields> response = ticketFormService.createTicketFields(newTicketFieldCustomDropDown);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketFields createdTicketField = response.getBody();
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
        ResponseEntity<TicketFields> response = ticketFormService.createTicketFields(newTicketFieldCustomText);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketFields createdTicketField = response.getBody();
            assertNotNull(createdTicketField);
            assertEquals("custom_text", createdTicketField.getType());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            LOGGER.info(objectMapper.writeValueAsString(createdTicketField));
        } else {
            LOGGER.error("Failed to create Ticket Fields");
        }
    }

//    @Test
//    @DisplayName("Update a Ticket Form by it's id")
//    @Order(4)
//    @Disabled
//    public void testUpdateTicketById() throws JsonProcessingException {
//        ResponseEntity<TicketForm> createdResponse = ticketFormService.createTicketForm(newTicketForm);
//        TicketForm createdTicketForm = createdResponse.getBody();
//
//        assert createdTicketForm != null;
//        ResponseEntity<TicketForm> updatedResponse = ticketFormService.updateTicketForm(createdTicketForm.getId(), updatedTicketForm);
//
//        HttpStatusCode httpStatus = updatedResponse.getStatusCode();
//        assertEquals(HttpStatus.OK, httpStatus);
//        assertNotEquals(updatedTicketForm.getTitle(), createdResponse.getBody().getTitle());
//        assertNotEquals(updatedTicketForm.getDescription(), createdResponse.getBody().getDescription());
//        assertNotEquals(updatedTicketForm.getFields(), createdResponse.getBody().getFields());
//        LOGGER.info(objectMapper.writeValueAsString(updatedResponse.getBody()));
//    }

//    @Test
//    @DisplayName("Delete a Ticket Form by its id")
//    @Disabled
//    @Order(5)
//    public void testDeleteTicketForm() throws JsonProcessingException {
//        ResponseEntity<TicketForm> createdResponse = ticketFormService.createTicketForm(newTicketForm);
//        TicketForm createdTicket = createdResponse.getBody();
//        assert createdTicket != null;
//        Long ticketFormId = createdTicket.getId();
//
//        ResponseEntity<String> deleteResponse = ticketFormService.deleteTicketForm(ticketFormId);
//        String message = deleteResponse.getBody();
//
//        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
//        assertEquals("Ticket [#"+ticketFormId+"] deleted successfully", message);
//
//        ResponseEntity<TicketForm> notFoundResponse = ticketFormService.getTicketFormById(ticketFormId);
////		it should be HttpStatus.NOT_FOUND but HttpStatus.OK is default behaviour or freshdesk API
//        assertEquals(HttpStatus.OK, notFoundResponse.getStatusCode());
//        LOGGER.info(objectMapper.writeValueAsString(notFoundResponse));
//    }
}
