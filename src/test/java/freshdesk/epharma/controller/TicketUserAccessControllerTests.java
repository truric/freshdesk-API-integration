package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketForm.TicketForm;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccess;
import freshdesk.epharma.model.TicketUserAccess.TicketUserAccessPatch;
import freshdesk.epharma.service.TicketUserAccessService;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketUserAccessControllerTests {
    @Autowired
    private TicketUserAccessService ticketUserAccessService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketUserAccessService.class);

    private final TicketUserAccess createNewTicketUserAccess = TestDataFactory.createNewTicketUserAccess();
    private final TicketUserAccessPatch createNewTicketUserAccessPatch = TestDataFactory.createNewTicketUserAccessPatch();

    Long ticketId = 90L;

    @Test
    @DisplayName("Get Ticket User Access")
    @Order(1)
    void testGetTicketUserAccess() throws JsonProcessingException {
        ResponseEntity<List<TicketUserAccess>> response = ticketUserAccessService.getTicketUserAccess(ticketId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println((objectMapper.writeValueAsString(response.getBody())));
    }

    @Test
    @DisplayName("Create a Ticket User Access")
    @Order(2)
    void testCreateTicketUserAccess() throws JsonProcessingException {
        ResponseEntity<TicketUserAccess> response = ticketUserAccessService.createTicketUserAccess(
                ticketId, createNewTicketUserAccess);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println((objectMapper.writeValueAsString(response.getBody())));
    }

    @Test
    @DisplayName("Update Ticket User Access")
    @Order(3)
    void testUpdateTicketUserAccess() throws JsonProcessingException {

        ResponseEntity<TicketUserAccessPatch> response = ticketUserAccessService.updateTicketUserAccess(
                ticketId, createNewTicketUserAccessPatch);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println((objectMapper.writeValueAsString(response.getBody())));
    }

    @Test
    @DisplayName("Delete Ticket User Access")
    @Order(4)
    void testDeleteTicketUserAccess() {
        ResponseEntity<TicketUserAccess> response = ticketUserAccessService.deleteTicketUserAccess(ticketId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        System.out.println("Ticket User Access deleted successfully");
    }

}
