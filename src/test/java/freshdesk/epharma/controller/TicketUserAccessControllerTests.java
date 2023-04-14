package freshdesk.epharma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.service.TicketUserAccessService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketUserAccessControllerTests {
    @Autowired
    private TicketUserAccessService ticketUserAccessService;

    @Autowired
    ObjectMapper objectMapper;
}
