package freshdesk.epharma.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.model.*;
import freshdesk.epharma.factory.TestDataFactory;
import freshdesk.epharma.service.TicketService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.file.Files;
import java.nio.file.Paths;
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

	@Test
	@DisplayName("Get Ticket list")
	@Order(1)
	void testGetAllTickets() {
		ResponseEntity<List<Ticket>> responseEntity = ticketService.getAllTickets();
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
		ResponseEntity<Ticket> response = ticketService.getTicketById(ticketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info("Retrieved ticket: {}", ticket);
		} else {
			LOGGER.error("Unable to retrieve ticket with ID " + ticketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Get a Ticket by it's id")
	@Disabled
//	The Archive Tickets feature(s) is/are not supported in your plan. Please upgrade your account to use it.
	void testGetArchivedTicketById() {
		long archivedTicketId = 2;
		ResponseEntity<Ticket> response = ticketService.getArchivedTicketById(archivedTicketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info("Retrieved archived ticket: {}", ticket);
		} else {
			LOGGER.error("Unable to retrieve archived ticket with ID " + archivedTicketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Get all conversations of archived ticket by id")
	@Disabled
//	The Archive Tickets feature(s) is/are not supported in your plan. Please upgrade your account to use it.
	void testGetAllConversationsOfArchivedTicketById() {
		long archivedTicketId = 2;
		ResponseEntity<Ticket> response = ticketService.getAllConversationsOfArchivedTicketById(archivedTicketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			LOGGER.info("All conversations retrieved from archived ticket: {}", ticket);
		} else {
			LOGGER.error("Unable to retrieve conversations from archived ticket with ID " + archivedTicketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	@DisplayName("Get Ticket list with pagination")
	@Order(3)
	void testGetTicketsWithPagination() {
		int pageNumber = 1;
		ResponseEntity<List<Ticket>> responseEntity = ticketService.getTicketsWithPagination(pageNumber);
		HttpStatusCode httpStatus = responseEntity.getStatusCode();
		if (httpStatus == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			LOGGER.info("Page number: " +  pageNumber + "\n");
			assert tickets != null;
			for (Ticket ticket : tickets) {
				LOGGER.info(ticket.toString());
			}
		} else {
			LOGGER.error("Failed to get tickets with pagination");
		}
	}

	@Test
	@DisplayName("Create a new Ticket")
	void testCreateTicket() {
		Ticket newTicket = TestDataFactory.createNewTicket();

		ResponseEntity<Ticket> response = ticketService.createTicket(newTicket);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getId());
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
	public void testUpdateTicketById() {
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
	}

	@Test
	@DisplayName("Bulk Update Tickets")
	public void testBulkUpdateTickets() {
		ResponseEntity<Ticket> createdResponse1 = ticketService.createTicket(newTicket);
		Ticket createdTicket1 = createdResponse1.getBody();
		ResponseEntity<Ticket> createdResponse2 = ticketService.createTicket(newTicket);
		Ticket createdTicket2 = createdResponse2.getBody();
		ResponseEntity<Ticket> createdResponse3 = ticketService.createTicket(newTicket);
		Ticket createdTicket3 = createdResponse3.getBody();

		Map<String, Ticket> properties = new HashMap<>();

		Ticket statusTicket1 = new Ticket();
		statusTicket1.setStatus(3);
		properties.put("status", statusTicket1);

		Ticket sourceTicket = new Ticket();
		sourceTicket.setSource(1);
		properties.put("source", sourceTicket);

		Ticket priorityTicket = new Ticket();
		priorityTicket.setPriority(4);
		properties.put("priority", priorityTicket);

		assert createdTicket1 != null;
		assert createdTicket2 != null;
		assert createdTicket3 != null;
		List<Long> ids = Arrays.asList(
				createdTicket1.getId(),
				createdTicket2.getId(),
				createdTicket3.getId()
		);

		TicketBulkUpdateResponse bulkUpdateRequest = new TicketBulkUpdateResponse(ids, properties, null);

		ResponseEntity<TicketBulkUpdateResponse> bulkUpdateResponseEntity = ticketService.bulkUpdateTickets(bulkUpdateRequest);

		assertNotNull(bulkUpdateResponseEntity);

		//TODO
		assertEquals(3, Objects.requireNonNull(bulkUpdateResponseEntity.getBody()).getIds().size());
		assertEquals(3, bulkUpdateResponseEntity.getBody().getProperties().size());
		assertEquals(3, bulkUpdateResponseEntity.getBody().getProperties().get("status").getStatus().intValue());
		assertEquals(1, bulkUpdateResponseEntity.getBody().getProperties().get("source").getSource().intValue());
		assertEquals(4, bulkUpdateResponseEntity.getBody().getProperties().get("priority").getPriority().intValue());
	}

	@Test
	@DisplayName("Delete a Ticket by it's id")
	@Order(7)
	public void testDeleteTicket() {
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
	}

	@Test
	@DisplayName("Delete an Archived Ticket by it's id")
	@Order(7)
//		The Archive Tickets feature(s) is/are not supported in your plan. Please upgrade your account to use it.
	public void testDeleteArchivedTicket() {
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
	}

	@Test
	@DisplayName("Restore a deleted Ticket by it's id")
	@Order(8)
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
	@Order(9)
	public void testFilterTicketsByQuery() {
		TicketQueryDTO query = new TicketQueryDTO();

		// Integer values test
		query.setPriority(3);
		Ticket filteredTicket = ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(filteredTicket.toString());

		// String values test
		query.setTag("TAG");
		ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(filteredTicket.toString());

		// LocalDate values test
		query.setCreatedAt(LocalDate.of(2023, 4, 1));
		ticketService.searchTickets(query);
		assertNotNull(filteredTicket);
		LOGGER.info(filteredTicket.toString());
	}

	@Test
	@DisplayName("Delete multiple Tickets in bulk")
	@Order(10)
	public void testDeleteTicketsInBulk() {
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
	}

	@Test
	@DisplayName("Create a new Ticket with an attachment")
	@Disabled
	public void testCreateTicketWithAttachment() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setSubject("Test ticket with attachment");
		ticket.setDescription("Test ticket description");

		byte[] fileBytes = Files.readAllBytes(Paths.get(ATTACHMENT_FILE_PATH));
		Resource attachment = new ByteArrayResource(fileBytes) {
			@Override
			public String getFilename() {
				return "pharma.jpeg";
			}
		};

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("ticket", ticket);
		body.add("attachment", attachment);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<Ticket> responseEntity = ticketService.createTicketWithAttachment(
				ticket, attachment);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getId());
	}
}