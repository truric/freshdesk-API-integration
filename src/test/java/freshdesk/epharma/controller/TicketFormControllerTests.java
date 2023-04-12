package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketFields.TicketField;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketFormControllerTests {

    @Autowired
    private TicketFormService ticketFormService;

    @Autowired
    ObjectMapper objectMapper;

    long ticketFormId = 103000143290L;
    long ticketFieldId = 103000878346L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketFormController.class);
    private final TicketForm newTicketForm = TestDataFactory.createNewTicketForm();
    private final TicketForm newTicketForm2 = TestDataFactory.createNewTicketForm2();
    private final String randomSting = TestDataFactory.randomStringGenerator();
    private final List<TicketField> mandatoryTicketFields = TestDataFactory.createMandatoryTicketFields();
    private final TicketField createNewTicketField = TestDataFactory.createNewTicketField();

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
    void testGetTicketFormById() throws JsonProcessingException {
        ResponseEntity<TicketForm> response = ticketFormService.getTicketFormById(ticketFormId);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
        } else {
            LOGGER.error("Unable to retrieve Ticket Form with ID " + ticketFormId + ". HTTP status: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Clone a Ticket Form by its id")
    void testCloneTicketFormById() throws JsonProcessingException {
        ResponseEntity<TicketForm> response = ticketFormService.cloneTicketFormById(ticketFormId);
        if (response.getStatusCode() == HttpStatus.OK) {
            TicketForm ticketForm = response.getBody();
            assert ticketForm != null;
            LOGGER.info(objectMapper.writeValueAsString(ticketForm));
        } else {
            LOGGER.error("Unable to clone Ticket Form with ID " + ticketFormId + ". HTTP status: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("View A Ticket Form's Field")
    void testViewTicketsFormField() throws JsonProcessingException {
        ResponseEntity<TicketField> response = ticketFormService.viewTicketFormsField(ticketFormId, ticketFieldId);
        if (response.getStatusCode() == HttpStatus.OK) {
            TicketField ticketField = response.getBody();
            assert ticketField != null;
            LOGGER.info(objectMapper.writeValueAsString(ticketField));
        } else {
            LOGGER.error("Unable to retrieve Ticket Form with ID " + ticketFormId + ". HTTP status: " + response.getStatusCode());
        }
    }

//    TicketForm object creating rules:
//    title must be unique
//    must have fields: requester, company, subject and description with their respective ids
    @Test
    @DisplayName("Create a new Ticket Form")
    @Order(3)
    void testCreateTicket() throws JsonProcessingException {
        ResponseEntity<TicketForm> response = ticketFormService.createTicketForm(newTicketForm);
        HttpStatusCode httpStatus = response.getStatusCode();

        if (httpStatus == HttpStatus.CREATED) {
            TicketForm createdTicketForm = response.getBody();
            assert createdTicketForm != null;
            LOGGER.info(objectMapper.writeValueAsString(createdTicketForm));
        } else {
            LOGGER.error("Failed to create Ticket Form");
        }
    }

    @Test
    @DisplayName("Update a Ticket Form by it's id")
    @Disabled
    public void testUpdateTicketFormById() throws JsonProcessingException {

        TicketForm ticketForm = new TicketForm();
        ticketForm.setTitle(randomSting);
        ticketForm.setDescription("Description test");
//        TODO not accepting fields[] update
//        ticketForm.setFields(mandatoryTicketFields);

        ResponseEntity<TicketForm> createResponse = ticketFormService.createTicketForm(ticketForm);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<TicketForm> createResponse2 = ticketFormService.createTicketForm(newTicketForm);
        assertEquals(HttpStatus.CREATED, createResponse2.getStatusCode());

        Objects.requireNonNull(createResponse2.getBody()).setTitle("Different title");

        ResponseEntity<TicketForm> updateResponse = ticketFormService.updateTicketForm(
                createResponse.getBody().getId(), createResponse2.getBody());
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        assertEquals(ticketForm.getTitle(), newTicketForm.getTitle());
        assertEquals(ticketForm.getDescription(), newTicketForm.getDescription());
        assertEquals(ticketForm.getFields(), newTicketForm.getFields());

        System.out.println("---->");
        LOGGER.info(objectMapper.writeValueAsString(ticketForm));
        LOGGER.info(objectMapper.writeValueAsString(newTicketForm));
    }

    @Test
    @DisplayName("Update a Ticket Form's field by id")
    @Disabled
    public void testUpdateTicketFormsFieldById() throws JsonProcessingException {
        ResponseEntity<TicketField> responseTicketField = ticketFormService.updateTicketFormsField(
                ticketFormId, ticketFieldId, createNewTicketField);
        assertEquals(HttpStatus.FORBIDDEN, responseTicketField.getStatusCode());

        ResponseEntity<TicketForm> responseTicketForm = ticketFormService.getTicketFormById(ticketFormId);
        assertEquals(HttpStatus.OK, responseTicketForm.getStatusCode());

        TicketForm ticketForm = objectMapper.convertValue(responseTicketField, TicketForm.class);

        LOGGER.info(objectMapper.writeValueAsString(ticketForm));
        LOGGER.info(objectMapper.writeValueAsString(Objects.requireNonNull(responseTicketForm.getBody()).getFields()));

    }

//    Note: Be cautious about deleting a ticket form since this action is irreversible.
//    You will not be able to restore a ticket form if you delete it.
    @Test
    @DisplayName("Delete a Ticket Form by its id")
    @Order(5)
    public void testDeleteTicketForm() throws JsonProcessingException {
        ResponseEntity<TicketForm> response = ticketFormService.createTicketForm(newTicketForm);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<String> deleteResponse = ticketFormService.deleteTicketForm(
                Objects.requireNonNull(response.getBody()).getId());

//		it should be HttpStatus.NOT_FOUND but HttpStatus.OK is default behaviour or freshdesk API
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}
