package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.TicketWatcher.MultiTicketWatcherResult;
import freshdesk.epharma.model.TicketWatcher.TicketWatcher;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherBulkUnwatchRequest;
import freshdesk.epharma.model.TicketWatcher.TicketWatcherResponse;
import freshdesk.epharma.service.TicketWatcherService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketWatcherControllerTests {
    @Autowired
    private TicketWatcherService ticketWatcherService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketWatcherController.class);
    private final TicketWatcher ticketWatcher = TestDataFactory.addWatcherToMultipleTickets();

    Long ticketId = 98L;
    Long userId = 103078968765L;
    TicketWatcherBulkUnwatchRequest ticketWatcherBulkUnwatchRequest = new TicketWatcherBulkUnwatchRequest(Arrays.asList(91, 90, 98));

    @Test
    @DisplayName("Get all Watchers from a Ticker")
    @Order(1)
    void testGetAllTicketWatchers() throws JsonProcessingException {
        ResponseEntity<List<TicketWatcherResponse>> response = ticketWatcherService.getAllTicketWatchers(ticketId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LOGGER.info(objectMapper.writeValueAsString(Objects.requireNonNull(response.getBody()).get(0).getWatcherIds()));
    }

    @Test
    @DisplayName("Create Watcher for a Ticket")
    @Order(2)
    @Disabled
    // Method not working property by API side
    void testCreateTicketWatcher() throws JsonProcessingException {
        ResponseEntity<String> response = ticketWatcherService.createTicketWatcher(ticketId, userId);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
    }

    @Test
    @DisplayName("Remove Watcher from Ticket")
    @Order(3)
    public void testRemoveWatcherFromTicker() {
        ResponseEntity<TicketWatcher> response = ticketWatcherService.removeTicketWatcher(ticketId);
        // Response should be 204 NO_CONTENT, but it's 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LOGGER.info("Watcher from Ticket #" + Objects.requireNonNull(ticketId) + " successfully removed");
    }

    @Test
    @DisplayName("Add Watcher to multiple Tickets")
    @Order(4)
    public void testAddWatcherToMultipleTickets() throws JsonProcessingException {
        ResponseEntity<MultiTicketWatcherResult> response = ticketWatcherService.addTicketWatcherToMultipleTickets(ticketWatcher);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
    }


    @Test
    @DisplayName("Remove Watcher from multiple Tickets")
    @Order(5)
    public void testRemoveWatcherFromMultipleTickets() throws JsonProcessingException {
        ResponseEntity<MultiTicketWatcherResult> responseAdd = ticketWatcherService.addTicketWatcherToMultipleTickets(ticketWatcher);
        assertEquals(HttpStatus.OK, responseAdd.getStatusCode());

        ResponseEntity<MultiTicketWatcherResult> responseDelete = ticketWatcherService.deleteTicketWatcherFromMultipleTickers(ticketWatcherBulkUnwatchRequest);
        // Response should be 204 NO_CONTENT, but it's 200 OK
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
    }
}
