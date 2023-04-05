package freshdesk.epharma.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import freshdesk.epharma.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.*;

public class TestDataFactory {

    @Value("${attachment.filepath}")
    private static String ATTACHMENT_FILE_PATH;

    @Value("${attachment.filepath2}")
    private static String ATTACHMENT_FILE_PATH_2;

    public static Ticket createNewTicket() {
        return new Ticket(
                "1234567890",
                2,
                2,
                3,
                "Ticket subject to restore",
                "Ticket description to restore",
                "Ticket name to restore"
        );
    }

    public static Ticket createUpdatedTicket() {
        return new Ticket(
                "0987654321",
                3,
                7,
                1,
                "Updated subject",
                "Updated description",
                "Updated name"
        );
    }

    public static Ticket createNewTicketWithAttachment() throws IOException {
        Resource resource = new FileSystemResource("src/main/resources/epharma.jpeg");
        byte[] attachmentData = StreamUtils.copyToByteArray(resource.getInputStream());

        TicketAttachment attachment = new TicketAttachment();
        attachment.setName("epharma.jpeg");
        attachment.setData(attachmentData);

        Ticket ticket = new Ticket();
        ticket.setSubject("Test Subject");
        ticket.setDescription("Test Description");
        ticket.setEmail("test@test.com");
        ticket.setStatus(2);
        ticket.setPriority(1);
        ticket.setAttachments(Collections.singletonList(attachment));

        return ticket;
    }

    public static Ticket createNewTicketWithMultiAttachments() throws IOException {
        Resource resource1 = new FileSystemResource("src/main/resources/epharma.jpeg");
        byte[] attachmentData1 = StreamUtils.copyToByteArray(resource1.getInputStream());
        TicketAttachment attachment1 = new TicketAttachment();
        attachment1.setName("attachment1.jpg");
        attachment1.setData(attachmentData1);

        Resource resource2 = new FileSystemResource("src/main/resources/equipa.jpeg");
        byte[] attachmentData2 = StreamUtils.copyToByteArray(resource2.getInputStream());
        TicketAttachment attachment2 = new TicketAttachment();
        attachment2.setName("attachment2.jpg");
        attachment2.setData(attachmentData2);

        List<TicketAttachment> attachments = new ArrayList<>();
        attachments.add(attachment1);
        attachments.add(attachment2);

        Ticket ticket = new Ticket();
        ticket.setSubject("Test Subject");
        ticket.setDescription("Test Description");
        ticket.setEmail("test@test.com");
        ticket.setStatus(2);
        ticket.setPriority(1);
        ticket.setAttachments(attachments);

        return ticket;
    }

    public static TicketForm createNewTicketForm() {
        List<TicketFields> fields = new ArrayList<>();

        TicketFields field1 = new TicketFields();
        field1.setCustomerCanEdit(true);
        field1.setLabelForCustomer("Label for customers test 1");
        field1.setDisplayedToCustomers(true);
        field1.setLabel("Label test 1");
        TicketFieldType type = TicketFieldType.valueOf("CUSTOM_TEXT");
        field1.setType(type);

        fields.add(field1);

//        TicketFields field2 = new TicketFields();
//        field2.setCustomerCanEdit(true);
//        field2.setLabelForCustomer("Label for customers test 2");
//        field2.setDisplayedToCustomers(true);
//        field2.setLabel("Label test 2");
//        field2.setPosition(1);
//        type = TicketFieldType.CUSTOM_DROPDOWN;
//        field2.setType(type);
//        TicketFieldChoices[] choices = new TicketFieldChoices[]{
//                new TicketFieldChoices("Refund", 1),
//                new TicketFieldChoices("Faulty Product", 2),
//                new TicketFieldChoices("Item Not Delivered", 3)
//        };
//
//        fields.add(field2);

        return new TicketForm("Ticket Form Title", "Ticket Form Description", fields);
    }

    public static TicketForm createUpdatedTicketForm() {
        List<TicketFields> fields = new ArrayList<>();

        TicketFields field3 = new TicketFields();
        field3.setId(2L);
        field3.setLabelForCustomer("Field 3");
        field3.setCustomerCanEdit(false);
        field3.setRequiredForCustomers(false);
        field3.setHintForCustomers("Hint for Field 3");
        field3.setPlaceholderForCustomers("Placeholder for Field 3");

        fields.add(field3);

        TicketFields field4 = new TicketFields();
        field4.setId(3L);
        field4.setLabelForCustomer("Field 4");
        field4.setCustomerCanEdit(false);
        field4.setRequiredForCustomers(false);
        field4.setHintForCustomers("Hint for Field 4");
        field4.setPlaceholderForCustomers("Placeholder for Field 4");

        fields.add(field4);

        return new TicketForm("Ticket Form Title", "Ticket Form Description", fields);
    }

    public static String serializeToJson(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
