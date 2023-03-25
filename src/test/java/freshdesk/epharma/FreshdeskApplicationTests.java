package freshdesk.epharma;

import freshdesk.epharma.controller.TicketController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FreshdeskApplicationTests {

	@Autowired
	private TicketController ticketController;

	@Value("${freshdesk.url.main}")
	private String MAIN_URL;

	@Test
	void getAllTickets() {
	}

}
