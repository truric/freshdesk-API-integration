package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.model.Ticket.*;
import freshdesk.epharma.model.TicketSummary.TicketSummary;
import freshdesk.epharma.service.TicketService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketControllerTests {
	@Value("${freshdesk.url.main}")
	private String MAIN_URL;

	@Value("${attachment.filepath}")
	private String ATTACHMENT_FILE_PATH;

	@Autowired
	private TicketService ticketService;

	@Autowired
	ObjectMapper objectMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);
	private final Ticket newTicket = TestDataFactory.createNewTicket();
	private final Ticket updatedTicket = TestDataFactory.createUpdatedTicket();
	private final Ticket newTicketWithAttachment = TestDataFactory.createNewTicketWithAttachment();
	private final Ticket newTicketWithMultiAttachments = TestDataFactory.createNewTicketWithMultiAttachments();
	private final TicketSummary newTicketSummary = TestDataFactory.createNewTicketSummary();

	TicketControllerTests() throws IOException {
	}

	@Test
	@DisplayName("Get Ticket list")
	@Order(1)
	void testGetAllTickets() throws JsonProcessingException {
		ResponseEntity<List<Ticket>> responseEntity = ticketService.getAllTickets();
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			assert tickets != null;
			for (Ticket ticket : tickets) {
				LOGGER.info(objectMapper.writeValueAsString(ticket));
			}

		} else {
			LOGGER.error("Failed to retrieve tickets, status code: {}", statusCode);
		}
	}

	@Test
	@DisplayName("Get a Ticket by it's id")
	@Order(2)
	void testGetTicketById() throws JsonProcessingException {
		long ticketId = 2;
		ResponseEntity<Ticket> response = ticketService.getTicketById(ticketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info(objectMapper.writeValueAsString(ticket));
		} else {
			LOGGER.error("Unable to retrieve ticket with ID " + ticketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Get a Tickets summary")
	@Order(3)
	void testGetTicketSummary() throws JsonProcessingException {
		ResponseEntity<Ticket> responseCreate = ticketService.createTicket(newTicket);
		assertEquals(HttpStatus.CREATED, responseCreate.getStatusCode());

		ResponseEntity<TicketSummary> responseUpdate = ticketService.updateTicketsSummary(
				Objects.requireNonNull(responseCreate.getBody()).getId(), newTicketSummary);
		assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());

		ResponseEntity<TicketSummary> responseSummary = ticketService.getTicketSummary(
				Objects.requireNonNull(responseCreate.getBody()).getId());
		assertEquals(HttpStatus.OK, responseSummary.getStatusCode());

		LOGGER.info(objectMapper.writeValueAsString(responseSummary.getBody()));
	}

	@Test
	@DisplayName("Get Ticket list with pagination")
	@Order(4)
	void testGetTicketsWithPagination() throws JsonProcessingException {
		int pageNumber = 1;
		ResponseEntity<List<Ticket>> responseEntity = ticketService.getTicketsWithPagination(pageNumber);
		HttpStatusCode httpStatus = responseEntity.getStatusCode();
		if (httpStatus == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			LOGGER.info("Page number: " +  pageNumber + "\n");
			assert tickets != null;
			for (Ticket ticket : tickets) {
				LOGGER.info(objectMapper.writeValueAsString(ticket));
			}
		} else {
			LOGGER.error("Failed to get tickets with pagination");
		}
	}

	@Test
	@DisplayName("Create a new Ticket")
	@Order(5)
	void testCreateTicket() throws JsonProcessingException {
		Ticket newTicket = TestDataFactory.createNewTicket();

		ResponseEntity<Ticket> response = ticketService.createTicket(newTicket);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getId());
		LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
	}

	@Test
	@DisplayName("Update a tickets summary")
	@Order(5)
	void testUpdateTicketsSummary() throws JsonProcessingException {
		ResponseEntity<Ticket> responseCreate = ticketService.createTicket(newTicket);
		assertEquals(HttpStatus.CREATED, responseCreate.getStatusCode());

		ResponseEntity<TicketSummary> responseUpdate = ticketService.updateTicketsSummary(
				Objects.requireNonNull(responseCreate.getBody()).getId(), newTicketSummary);
		assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());

		LOGGER.info(objectMapper.writeValueAsString(responseUpdate.getBody()));
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
	 * 		ResponseEntity<Ticket> savedTicket = ticketService.getTicketById(newTicket.getId());
	 * 		assertNotNull(savedTicket);
	 * 		assertEquals(newTicket.getSubject(), savedTicket.getBody().getSubject());
	 *
	 * 		mockServer.expect(requestTo(uri))
	 * 				.andExpect(method(HttpMethod.POST))
	 * 				.andRespond(withStatus(HttpStatus.CREATED));
	 */

	@Test
	@DisplayName("Update a Ticket by it's id")
	@Order(6)
	public void testUpdateTicketById() throws JsonProcessingException {
		ResponseEntity<Ticket> createdResponse = ticketService.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();

		assert createdTicket != null;
		ResponseEntity<Ticket> updatedResponse = ticketService.updateTicket(createdTicket.getId(), updatedTicket);

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
		LOGGER.info(objectMapper.writeValueAsString(updatedResponse.getBody()));
	}

	@Test
	@DisplayName("Delete a Ticket by it's id")
	@Order(7)
	public void testDeleteTicket() throws JsonProcessingException {
		ResponseEntity<Ticket> createdResponse = ticketService.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();
		assert createdTicket != null;
		Long ticketId = createdTicket.getId();

		ResponseEntity<String> deleteResponse = ticketService.deleteTicket(ticketId);
		String message = deleteResponse.getBody();

		assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
		assertEquals("Ticket [#"+ticketId+"] deleted successfully", message);

		ResponseEntity<Ticket> notFoundResponse = ticketService.getTicketById(ticketId);

//		it should be HttpStatus.NOT_FOUND
//		but HttpStatus.OK is default behaviour or freshdesk API
		assertEquals(HttpStatus.OK, notFoundResponse.getStatusCode());
		LOGGER.info(objectMapper.writeValueAsString(notFoundResponse));
	}

	@Test
	@DisplayName("Delete a Tickets summary")
	@Order(8)
	public void testDeleteTicketsSummary() {
		ResponseEntity<Ticket> responseCreate = ticketService.createTicket(newTicket);
		assertEquals(HttpStatus.CREATED, responseCreate.getStatusCode());

		ResponseEntity<TicketSummary> responseUpdate = ticketService.updateTicketsSummary(
				Objects.requireNonNull(responseCreate.getBody()).getId(), newTicketSummary);
		assertEquals(HttpStatus.OK, responseUpdate.getStatusCode());

		ResponseEntity<Ticket> responseDelete = ticketService.deleteTicketsSummary(
				Objects.requireNonNull(responseCreate.getBody()).getId());
		assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
		LOGGER.info("Tickets summary #" + Objects.requireNonNull(responseUpdate.getBody()).getId() + " successfully deleted");
	}

	@Test
	@DisplayName("Restore a deleted Ticket by it's id")
	@Order(9)
	public void testRestoreDeletedTicket() throws ResourceNotFoundException, JsonProcessingException {
		ResponseEntity<Ticket> createdResponse = ticketService.createTicket(newTicket);
		Ticket createdTicket = createdResponse.getBody();
		assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
		assert createdTicket != null;
		assertNotNull(createdTicket.getId());

		ticketService.deleteTicket(createdTicket.getId());

		ResponseEntity<String> restoredResponse = ticketService.restoreDeletedTicket(createdTicket.getId());
		assertEquals(HttpStatus.OK, restoredResponse.getStatusCode());
		assertEquals("Ticket [#"+createdTicket.getId()+"] restored successfully", restoredResponse.getBody());

		ResponseEntity<Ticket> restoredTicket = ticketService.getTicketById(createdTicket.getId());
		assertEquals(HttpStatus.OK, restoredTicket.getStatusCode());

		Ticket restoredTicket2 = restoredTicket.getBody();

		assertNotNull(restoredTicket);

		assert restoredTicket2 != null;
		assertEquals(newTicket.getSource(), restoredTicket2.getSource());
		assertEquals(newTicket.getStatus(), restoredTicket2.getStatus());
		assertEquals(newTicket.getPriority(), restoredTicket2.getPriority());
		assertEquals(newTicket.getSubject(), restoredTicket2.getSubject());
//		newTicket.getDescription() String value has HTML div tag e.a. "<div>...text...</div>" and restoredTicket2 does not
		assertTrue(restoredTicket2.getDescription().contains(newTicket.getDescription()));
		LOGGER.info(objectMapper.writeValueAsString(restoredTicket2));
	}

	@Test
	@DisplayName("Filter Tickets by query")
	@Order(10)
	public void testFilterTicketsByQuery() throws JsonProcessingException {
		TicketQueryDTO query = new TicketQueryDTO();

		// Integer values test
		query.setPriority(3);
		Ticket filteredTicket = ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(objectMapper.writeValueAsString(filteredTicket));

		// String values test
		query.setTag("TAG");
		ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(objectMapper.writeValueAsString(filteredTicket));

		// LocalDate values test
		query.setCreatedAt(LocalDate.of(2023, 4, 1));
		ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(objectMapper.writeValueAsString(filteredTicket));
	}

	@Test
	@DisplayName("Delete multiple Tickets in bulk")
	@Order(11)
	public void testDeleteTicketsInBulk() throws JsonProcessingException {
		ResponseEntity<Ticket> createdResponse1 = ticketService.createTicket(newTicket);
		ResponseEntity<Ticket> createdResponse2 = ticketService.createTicket(newTicket);
		ResponseEntity<Ticket> createdResponse3 = ticketService.createTicket(newTicket);

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

		ResponseEntity<String> deleteResponse = ticketService.deleteTicketsInBulk(bulkAction);
		String message = deleteResponse.getBody();

		assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
		assertEquals("Tickets " + ids + " deleted successfully", message);

		ResponseEntity<Ticket> notFoundResponse1 = ticketService.getTicketById(createdTicket1.getId());
		ResponseEntity<Ticket> notFoundResponse2 = ticketService.getTicketById(createdTicket2.getId());
		ResponseEntity<Ticket> notFoundResponse3 = ticketService.getTicketById(createdTicket3.getId());

		assertEquals(HttpStatus.OK, notFoundResponse1.getStatusCode());
		assertEquals(HttpStatus.OK, notFoundResponse2.getStatusCode());
		assertEquals(HttpStatus.OK, notFoundResponse3.getStatusCode());
		LOGGER.info(objectMapper.writeValueAsString(message));

	}

	@Test
	@DisplayName("Create a new Ticket with an attachment")
	@Order(12)
	void testCreateTicketWithAttachmentTest() throws JsonProcessingException {
		ResponseEntity<Ticket> response = ticketService.createTicketWithAttachment(newTicketWithAttachment);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(newTicketWithAttachment.getSubject(), Objects.requireNonNull(response.getBody()).getSubject());
		assertNotNull(response.getBody().getId());
		LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
	}

	@Test
	@DisplayName("Create a new Ticket with multi attachments")
	@Order(13)
	void testCreateTicketWithMultiAttachmentTest() throws JsonProcessingException {
		ResponseEntity<Ticket> response = ticketService.createTicketWithAttachment(newTicketWithMultiAttachments);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(newTicketWithMultiAttachments.getSubject(), Objects.requireNonNull(response.getBody()).getSubject());
		assertNotNull(response.getBody().getId());
		LOGGER.info(objectMapper.writeValueAsString(response.getBody()));
	}

	@Test
	@DisplayName("Delete an attachment")
	@Order(14)
	void testDeleteAnAttachmentTest() throws JsonProcessingException {
		ResponseEntity<Ticket> responseCreate = ticketService.createTicketWithAttachment(newTicketWithAttachment);
		assertEquals(HttpStatus.CREATED, responseCreate.getStatusCode());

		Long attachmentId = Objects.requireNonNull(responseCreate.getBody()).getAttachments().get(0).getId();

		ResponseEntity<TicketAttachment> responseDelete = ticketService.deleteAnAttachment(attachmentId);

		if (HttpStatus.OK == responseDelete.getStatusCode()) {
			LOGGER.info("Attachment #"+ attachmentId + " successfully deleted");
		} else {
			LOGGER.error("Failed to delete attachment");
		}
	}

	@Test
	@DisplayName("Update Tickets in bulk")
	@Order(15)
	public void testUpdateTicketsInBulk() {
		TicketBulkUpdate bulkAction = new TicketBulkUpdate();
		bulkAction.setIds(Arrays.asList(15, 26, 37));

		Map<String, Integer> properties = new HashMap<>();
		properties.put("status", 2);
		properties.put("priority", 4);
		properties.put("source", 1);

		TicketReply reply = new TicketReply();
		reply.setBody("body goes here for testing");

		bulkAction.setProperties(properties);
		bulkAction.setTicketReply(reply);

		ResponseEntity<String> responseEntity = ticketService.bulkUpdateTickets(bulkAction);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		System.out.println(responseEntity.getBody());
	}

	@Test
	@DisplayName("Get all conversations of archived ticket by id")
	@Disabled
//	The Archive Tickets feature(s) is/are not supported in your plan. Please upgrade your account to use it.
	void testGetAllConversationsOfArchivedTicketById() throws JsonProcessingException {
		long archivedTicketId = 2;
		ResponseEntity<Ticket> response = ticketService.getAllConversationsOfArchivedTicketById(archivedTicketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info(objectMapper.writeValueAsString(ticket));
		} else {
			LOGGER.error("Unable to retrieve conversations from archived ticket with ID " + archivedTicketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Delete an Archived Ticket by its id")
	@Disabled
//		The Archive Tickets feature(s) is/are not supported in your plan. Please upgrade your account to use it.
	public void testDeleteArchivedTicket() throws JsonProcessingException {
		ResponseEntity<Ticket> createdResponse = ticketService.createTicket(newTicket);
		Ticket archivedTicket = createdResponse.getBody();
		assert archivedTicket != null;
		Long archivedTicketId = archivedTicket.getId();

		ResponseEntity<String> deleteResponse = ticketService.deleteArchivedTicket(archivedTicket.getId());
		String message = deleteResponse.getBody();

		assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
		assertEquals("Archived Ticket [#"+archivedTicketId+"] deleted successfully", message);

		ResponseEntity<Ticket> notFoundResponse = ticketService.getTicketById(archivedTicketId);

//		it should be HttpStatus.NOT_FOUND
//		but HttpStatus.OK is default behaviour or freshdesk API
		assertEquals(HttpStatus.OK, notFoundResponse.getStatusCode());
		LOGGER.info(objectMapper.writeValueAsString(notFoundResponse));
	}

}