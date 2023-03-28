package freshdesk.epharma.controller;

import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketQueryDTO;
import freshdesk.epharma.factory.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketControllerTests {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TicketController ticketController;

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);
	private final Ticket newTicket = TestDataFactory.createNewTicket();
	private final Ticket updatedTicket = TestDataFactory.createUpdatedTicket();

	@Test
	@DisplayName("Get Ticket list")
	@Order(1)
	void testGetAllTickets() {
		ResponseEntity<List<Ticket>> responseEntity = ticketController.getAllTickets();
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			assert tickets != null;
			for (Ticket ticket : tickets) {
				LOGGER.info(ticket.toString());
			}

		} else {
			LOGGER.error("Failed to retrieve tickets, status code: {}", statusCode);
		}
	}

	@Test
	@DisplayName("Get a Ticket by it's id")
	@Order(2)
	void testGetTicketById() {
		long ticketId = 2;
		ResponseEntity<Ticket> response = ticketController.getTicketById(ticketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info("Retrieved ticket: {}", ticket.toString());
		} else {
			LOGGER.error("Unable to retrieve ticket with ID " + ticketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Get Ticket list with pagination")
	@Order(3)
	void testGetTicketsWithPagination() {
		int pageNumber = 1;
		ResponseEntity<List<Ticket>> responseEntity = ticketController.getTicketsWithPagination(pageNumber);
		HttpStatusCode httpStatus = responseEntity.getStatusCode();
		if (httpStatus == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			LOGGER.info("Page number: " +  pageNumber + "\n");
			for (Ticket ticket : tickets) {
				LOGGER.info(ticket.toString());
			}
		} else {
			LOGGER.error("Failed to get tickets with pagination");
		}
	}

	@Test
	@DisplayName("Create a new Ticket")
	@Order(4)
	void testCreateTicket() {
		ResponseEntity<Ticket> response = ticketController.createTicket(newTicket);
		HttpStatusCode httpStatus = response.getStatusCode();

		if (httpStatus == HttpStatus.CREATED) {
			Ticket createdTicket = response.getBody();
			LOGGER.info(createdTicket.toString());
		} else {
			LOGGER.error("Failed to create ticket");
		}
	}

	/**
	 * this will be the mockito approach
	 *
	 * private MockRestServiceServer mockServer;
	 * @Value("${freshdesk.url.main}")
	 * private String MAIN_URL;
	 *
	 * @BeforeEach
	 * public void setup() {
	 * 		mockServer = MockRestServiceServer.createServer(new RestTemplateBuilder().build());
	 * }
	 *
	 * ......
	 * 		URI uri = UriComponentsBuilder.fromUriString(MAIN_URL)
	 * 				.path("tickets")
	 * 				.build()
	 * 				.toUri();
	 *
	 *
	 * 		// Mock the response from the external API
	 * 		mockServer.expect(requestTo(uri))
	 * 				.andExpect(method(HttpMethod.POST))
	 * 				.andRespond(withStatus(HttpStatus.CREATED));
	 *
	 * 		ResponseEntity<String> response = restTemplate.postForEntity(uri, newTicket, String.class);
	 * 		assertEquals(HttpStatus.OK, response.getStatusCode());
	 *
	 * 		// Verify that the ticket was created in the database
	 * 		ResponseEntity<Ticket> savedTicket = ticketController.getTicketById(newTicket.getId());
	 * 		assertNotNull(savedTicket);
	 * 		assertEquals(newTicket.getSubject(), savedTicket.getBody().getSubject());
	 *
	 * 		mockServer.expect(requestTo(uri))
	 * 				.andExpect(method(HttpMethod.POST))
	 * 				.andRespond(withStatus(HttpStatus.CREATED));
	 */

	@Test
	@DisplayName("Update a Ticket by it's id")
	@Order(5)
	public void testUpdateTicketById() {
		ResponseEntity<Ticket> createdResponse = ticketController.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();

		assert createdTicket != null;
		ResponseEntity<Ticket> updatedResponse = ticketController.updateTicket(createdTicket.getId(), updatedTicket);
		Ticket responseTicket = updatedResponse.getBody();

		HttpStatusCode httpStatus = updatedResponse.getStatusCode();
		assertEquals(HttpStatus.OK, httpStatus);
		assertNotEquals(updatedTicket.getPhone(), createdResponse.getBody().getPhone());
		assertNotEquals(updatedTicket.getSource(), createdResponse.getBody().getSource());
		assertNotEquals(updatedTicket.getStatus(), createdResponse.getBody().getStatus());
		assertNotEquals(updatedTicket.getPriority(), createdResponse.getBody().getPriority());
		assertNotEquals(updatedTicket.getSubject(), createdResponse.getBody().getSubject());
		assertNotEquals(updatedTicket.getDescription(), createdResponse.getBody().getDescription());
		assertNotEquals(updatedTicket.getName(), createdResponse.getBody().getName());
		assertEquals(createdTicket.getRequesterId(), createdResponse.getBody().getRequesterId());
	}

	@Test
	@DisplayName("Delete a Ticket by it's id")
	@Order(6)
	public void testDeleteTicket() throws Exception {
		ResponseEntity<Ticket> createdResponse = ticketController.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();
		Long ticketId = createdTicket.getId();

		ResponseEntity<String> deleteResponse = ticketController.deleteTicket(ticketId);
		String message = deleteResponse.getBody();

		assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
		assertEquals("Ticket [#"+ticketId+"] deleted successfully", message);

		ResponseEntity<Ticket> notFoundResponse = ticketController.getTicketById(ticketId);
//		it should be HttpStatus.NOT_FOUND but HttpStatus.OK is default behaviour or freshdesk API
		assertEquals(HttpStatus.OK, notFoundResponse.getStatusCode());
	}

	@Test
	@DisplayName("Restore a deleted Ticket by it's id")
	@Order(7)
	public void testRestoreDeletedTicket() throws ResourceNotFoundException {
		ResponseEntity<Ticket> createdResponse = ticketController.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();
		assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
		assertNotNull(createdTicket.getId());

		ticketController.deleteTicket(createdTicket.getId());

		ResponseEntity<String> restoredResponse = ticketController.restoreDeletedTicket(createdTicket.getId());
		assertEquals(HttpStatus.OK, restoredResponse.getStatusCode());
		assertEquals("Ticket [#"+createdTicket.getId()+"] restored successfully", restoredResponse.getBody());

		ResponseEntity<Ticket> restoredTicket = ticketController.getTicketById(createdTicket.getId());
		assertEquals(HttpStatus.OK, restoredTicket.getStatusCode());

		Ticket restoredTicket2 = restoredTicket.getBody();

		assertNotNull(restoredTicket);

		assertEquals(newTicket.getSource(), restoredTicket2.getSource());
		assertEquals(newTicket.getStatus(), restoredTicket2.getStatus());
		assertEquals(newTicket.getPriority(), restoredTicket2.getPriority());
		assertEquals(newTicket.getSubject(), restoredTicket2.getSubject());
//		newTicket.getDescription() String value has HTML div tag e.a. "<div>...text...</div>" and restoredTicket2 does not
		assertTrue(restoredTicket2.getDescription().contains(newTicket.getDescription()));
	}

	@Test
	@DisplayName("Filter Tickets by query")
	@Order(8)
	public void testFilterTicketsByQuery() throws Exception {
		TicketQueryDTO query = new TicketQueryDTO();
		query.setQuery("priority:3");

		Ticket filteredTicket = ticketController.searchTickets(query);

		assertNotNull(filteredTicket);
		LOGGER.info(filteredTicket.toString());
	}

	@Test
	@DisplayName("Delete multiple Tickets in bulk")
	@Order(9)
	public void testDeleteTicketsInBulk() throws Exception {
		ResponseEntity<Ticket> createdResponse1 = ticketController.createTicket(newTicket);
		ResponseEntity<Ticket> createdResponse2 = ticketController.createTicket(newTicket);
		ResponseEntity<Ticket> createdResponse3 = ticketController.createTicket(newTicket);

		Ticket createdTicket1 = createdResponse1.getBody();
		Ticket createdTicket2 = createdResponse2.getBody();
		Ticket createdTicket3 = createdResponse3.getBody();

		assert createdTicket1 != null;
		assert createdTicket2 != null;
		assert createdTicket3 != null;
		List<Long> ids = Stream.of(createdTicket1.getId(), createdTicket2.getId(), createdTicket3.getId())
				.collect(Collectors.toList());

		Map<String, List<Long>> bulkAction = new HashMap<>();
		bulkAction.put("ids", ids);

		ResponseEntity<String> deleteResponse = ticketController.deleteTicketsInBulk(bulkAction);
		String message = deleteResponse.getBody();

		assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
		assertEquals("Tickets " + ids + " deleted successfully", message);

		ResponseEntity<Ticket> notFoundResponse1 = ticketController.getTicketById(createdTicket1.getId());
		ResponseEntity<Ticket> notFoundResponse2 = ticketController.getTicketById(createdTicket2.getId());
		ResponseEntity<Ticket> notFoundResponse3 = ticketController.getTicketById(createdTicket3.getId());

		assertEquals(HttpStatus.OK, notFoundResponse1.getStatusCode());
		assertEquals(HttpStatus.OK, notFoundResponse2.getStatusCode());
		assertEquals(HttpStatus.OK, notFoundResponse3.getStatusCode());
	}

}