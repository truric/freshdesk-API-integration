package freshdesk.epharma;

import freshdesk.epharma.controller.TicketController;
import freshdesk.epharma.model.Ticket;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FreshdeskApplicationTests {

	@Autowired
	private TicketController ticketController;

	@Autowired
	RestTemplate restTemplate;

	@Value("${freshdesk.url.main}")
	private String MAIN_URL;

	@Test
	void getAllTickets() {
		ResponseEntity<List<Ticket>> responseEntity = ticketController.getAllTickets();
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			List<Ticket> tickets = responseEntity.getBody();
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", "ID", "Name", "Subject", "Phone", "Priority", "Status");

			assert tickets != null;
			for (Ticket ticket : tickets) {
				System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getId(), ticket.getName(),
						ticket.getSubject(), ticket.getPhone(), ticket.getPriority(), ticket.getStatus());
			}

		} else {
			System.out.println("Failed to retrieve tickets, status code: " + statusCode);
		}
	}

	@Test
	void getTicketById() {
		long ticketId = 1;
		ResponseEntity<Ticket> response = ticketController.getTicketById(ticketId);
		if (response.getStatusCode() == HttpStatus.OK) {
			Ticket ticket = response.getBody();
			assert ticket != null;
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", "ID", "Name", "Subject", "Phone", "Priority", "Status");
			System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getId(), ticket.getName(),
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
				System.out.printf("%-10s %-20s %-50s %-15s %-10s %-15s%n", ticket.getId(), ticket.getName(),
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
	@Disabled
//	TODO
	void createTicket() {
		ticketController.createTicket(new Ticket());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Ticket> response = restTemplate.exchange(
				MAIN_URL,
				HttpMethod.POST,
				requestEntity,
				Ticket.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
//        assertEquals("John Smith", response.getBody().getName());
//        assertEquals("Example subject", response.getBody().getSubject());
//        assertEquals(2, response.getBody().getPriority());
//        assertEquals(1, response.getBody().getStatus());
//        assertEquals("1234567890", response.getBody().getPhone());
	}


	@Test
	@Disabled
//	TODO
	void updateTicket() {
		ticketController.updateTicket(1L, new Ticket());
	}

	@Test
	@Disabled
//	TODO
	void deleteTicketById() {
		int ticketIdToDelete = 3;
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

}
