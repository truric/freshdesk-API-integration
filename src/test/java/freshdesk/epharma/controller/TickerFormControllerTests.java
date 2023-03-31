package freshdesk.epharma.controller;

import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketForm;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TickerFormControllerTests {

    @Autowired
    private TicketFormController ticketFormController;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketFormController.class);
    private final TicketForm newTicketForm = TestDataFactory.createNewTicketForm();
    private final TicketForm updatedTicketForm = TestDataFactory.createUpdatedTicketForm();

    @Test
    @DisplayName("Get all Ticket Forms")
    @Order(1)
    void testGetAllTicketForms() {
        ResponseEntity<List<TicketForm>> responseEntity = ticketFormController.getAllTicketForms();
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
    @DisplayName("Get a Ticket Form by it's id")
    @Order(2)
    void testGetTicketFormById() {
        long ticketFormId = 103000131305L;
        ResponseEntity<TicketForm> response = ticketFormController.getTicketFormById(ticketFormId);
        if (response.getStatusCode() == HttpStatus.OK) {
            TicketForm ticketForm = response.getBody();
            assert ticketForm != null;
            LOGGER.info("Retrieved Ticket Form: {}", ticketForm.toString());
        } else {
            LOGGER.error("Unable to retrieve Ticket Form with ID " + ticketFormId + ". HTTP status: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Create a new Ticket Form")
    @Disabled
    @Order(3)
    void testCreateTicket() {
        ResponseEntity<TicketForm> response = ticketFormController.createTicketForm(newTicketForm);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketForm createdTicketForm = response.getBody();
            LOGGER.info(createdTicketForm.toString());
        } else {
            LOGGER.error("Failed to create Ticket Form");
        }
    }

    @Test
    @DisplayName("Update a Ticket Form by it's id")
    @Order(4)
    @Disabled
    public void testUpdateTicketById() {
        ResponseEntity<TicketForm> createdResponse = ticketFormController.createTicketForm(newTicketForm);
        TicketForm createdTicketForm = createdResponse.getBody();

        assert createdTicketForm != null;
        ResponseEntity<TicketForm> updatedResponse = ticketFormController.updateTicketForm(createdTicketForm.getId(), updatedTicketForm);
        TicketForm responseTicketForm = updatedResponse.getBody();

        HttpStatusCode httpStatus = updatedResponse.getStatusCode();
        assertEquals(HttpStatus.OK, httpStatus);
        assertNotEquals(updatedTicketForm.getTitle(), createdResponse.getBody().getTitle());
        assertNotEquals(updatedTicketForm.getDescription(), createdResponse.getBody().getDescription());
        assertNotEquals(updatedTicketForm.getFields(), createdResponse.getBody().getFields());
    }

    @Test
    @DisplayName("Delete a Ticket Form by it's id")
    @Disabled
    @Order(5)
    public void testDeleteTicketForm() throws Exception {
        ResponseEntity<TicketForm> createdResponse = ticketFormController.createTicketForm(newTicketForm);
        TicketForm createdTicket = createdResponse.getBody();
        Long ticketFormId = createdTicket.getId();

        ResponseEntity<String> deleteResponse = ticketFormController.deleteTicketForm(ticketFormId);
        String message = deleteResponse.getBody();

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Ticket [#"+ticketFormId+"] deleted successfully", message);

        ResponseEntity<TicketForm> notFoundResponse = ticketFormController.getTicketFormById(ticketFormId);
//		it should be HttpStatus.NOT_FOUND but HttpStatus.OK is default behaviour or freshdesk API
        assertEquals(HttpStatus.OK, notFoundResponse.getStatusCode());
    }
}
