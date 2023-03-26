package freshdesk.epharma;

import freshdesk.epharma.controller.TicketController;
import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FreshdeskApplicationTests {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TicketController ticketController;

	private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

	@Value("${freshdesk.url.main}")
	private String MAIN_URL;

	private MockRestServiceServer mockServer;

	@BeforeEach
	public void setup() {
		mockServer = MockRestServiceServer.createServer(new RestTemplateBuilder().build());
	}

	@Test
	void getAllTickets() {
		ResponseEntity<List<Ticket>> responseEntity = ticketController.getAllTickets();
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", "ID", "Name", "Subject", "Phone", "Priority", "Status");

			assert tickets != null;
			for (Ticket ticket : tickets) {
				System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getPhone(), ticket.getName(),
						ticket.getSubject(), ticket.getPhone(), ticket.getPriority(), ticket.getStatus());
			}

		} else {
			System.out.println("Failed to retrieve tickets, status code: " + statusCode);
		}
	}

	@Test
	void getTicketById() {
		long ticketId = 2;
		ResponseEntity<Ticket> response = ticketController.getTicketById(ticketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", "ID", "Name", "Subject", "Phone", "Priority", "Status");
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getPhone(), ticket.getName(),
					ticket.getSubject(), ticket.getPhone(), ticket.getPriority(), ticket.getStatus());
		} else {
			System.out.println("Unable to retrieve ticket with ID " + ticketId + ". HTTP status: " + response.getStatusCode());
		}
	}

	@Test
	void getTicketsWithPagination() {
		int pageNumber = 1;
		ResponseEntity<List<Ticket>> responseEntity = ticketController.getTicketsWithPagination(pageNumber);
		HttpStatusCode httpStatus = responseEntity.getStatusCode();
		if (httpStatus == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			System.out.println("Page number: " +  pageNumber + "\n");
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", "ID", "Name", "Subject", "Phone", "Priority", "Status");
			for (Ticket ticket : tickets) {
				System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getPhone(), ticket.getName(),
						ticket.getSubject(), ticket.getPhone(), ticket.getPriority(), ticket.getStatus());
			}
		} else {
			fail("Failed to get tickets with pagination");
		}
	}

	@Test
	@Disabled
//	TODO
	void restoreDeletedTicket() {
		ticketController.restoreDeletedTicket(2L);
	}

	@Test
	void createTicket() {
		Ticket newTicket = new Ticket("11223344", 1, 2, 1,
				"Ticket subject", "Ticket description", "Ticket name");

		ResponseEntity<TicketResponse> response = ticketController.createTicket(newTicket);
		HttpStatusCode httpStatus = response.getStatusCode();

		if (httpStatus == HttpStatus.CREATED) {
			TicketResponse createdTicket = response.getBody();
			System.out.println(createdTicket);
		} else {
			fail("Failed to create ticket");
		}
	}


	/**
	 * this will be the mockito approach
	 *
	 * private MockRestServiceServer mockServer;
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
	void updateTicket() throws ResourceNotFoundException {
		Ticket newTicket = new Ticket("11223344", 1, 2, 1,
				"Ticket subject", "Ticket description", "Ticket name");
		ResponseEntity<TicketResponse> createResponse = ticketController.createTicket(newTicket);

		String responseBody = String.valueOf(createResponse.getBody());
		/**
		 * API doesn't allow id to be a property
		 * have to get id from full json object
		 * every property that are returned are the ones set up
		 * meanwhile, postman returns full json object, id inclusive
		 */
		int startIndex = responseBody.indexOf("\"id\": \"") + 7;
		int endIndex = responseBody.indexOf(",", startIndex);
		if (endIndex < 0 || endIndex > responseBody.length()) {
			endIndex = responseBody.length() - 1;
		}
		String ticketIdString = responseBody.substring(startIndex, endIndex);
		Long ticketId = Long.parseLong(ticketIdString);

		Ticket updatedTicket = new Ticket("99887766", 2, 1, 2,
				"Updated subject", "Updated description", "Updated name");
		ResponseEntity<Ticket> updateResponse = ticketController.updateTicket(ticketId, updatedTicket);

		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

		ResponseEntity<Ticket> getResponse = ticketController.getTicketById(ticketId);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());

		Ticket returnedTicket = getResponse.getBody();
		assertEquals(updatedTicket.getPhone(), returnedTicket.getPhone());
		assertEquals(updatedTicket.getPriority(), returnedTicket.getPriority());
		assertEquals(updatedTicket.getSubject(), returnedTicket.getSubject());
		assertEquals(updatedTicket.getDescription(), returnedTicket.getDescription());
		assertEquals(updatedTicket.getName(), returnedTicket.getName());

//		assertEquals(ticketId, returnedTicket.getId());
	}

	@Test
	@Disabled
//	TODO
	void deleteTicketById() {
		int ticketIdToDelete = 2;
		ResponseEntity<Void> response = restTemplate.exchange(
				MAIN_URL + "/tickets/" + ticketIdToDelete,
				HttpMethod.DELETE,
				null,
				Void.class
		);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());

		// Verify that the ticket has been deleted
		ResponseEntity<Ticket> getResponse = restTemplate.getForEntity(
				MAIN_URL + "/tickets/" + ticketIdToDelete,
				Ticket.class
		);
		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
	}

//	@Test
//	void anotherDeleteByIdApproach() {
//		// Create a new ticket to delete
//		Ticket newTicket = new Ticket("1234567890", 1, 1, 1,
//				"Ticket subject", "Ticket description", "Ticket name");
//		ResponseEntity<String> createResponse = ticketController.createTicket(newTicket);
//		assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
//		assertNotNull(newTicket);
//
//		// Delete the created ticket
//		ResponseEntity<Void> deleteResponse = restTemplate.exchange(
//				MAIN_URL + "/tickets/" + newTicket.getId(),
//				HttpMethod.DELETE,
//				null,
//				Void.class
//		);
//		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
//		assertNull(deleteResponse.getBody());
//		assertNull(newTicket);
//
//		// Verify that the ticket has been deleted
//		ResponseEntity<Ticket> getResponse = restTemplate.getForEntity(
//				MAIN_URL + "/tickets/" + newTicket.getId(),
//				Ticket.class
//		);
//		assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
//		assertNull(getResponse.getBody());
//	}

}
