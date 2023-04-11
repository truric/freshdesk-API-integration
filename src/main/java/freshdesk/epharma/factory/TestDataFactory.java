package freshdesk.epharma.factory;

import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.Ticket.TicketAttachment;
import freshdesk.epharma.model.TicketFields.TicketField;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
import freshdesk.epharma.model.TicketForm.TicketForm;
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

    public static Map<String, Object> createNewTicketFieldCustomDropdown() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("customer_can_edit", true);
        fieldMap.put("label_for_customers", "Label for customers test");
        fieldMap.put("displayed_to_customers", true);
        fieldMap.put("label", "Label test 123");
        fieldMap.put("position", 1);
        fieldMap.put("type", "custom_dropdown");

        List<TicketFieldChoices> choicesList = new ArrayList<>();
        choicesList.add(new TicketFieldChoices("Refund test 123", 3));
        choicesList.add(new TicketFieldChoices("Faulty Product test 123", 2));
        choicesList.add(new TicketFieldChoices("Item Not Delivered test 123", 1));

        Map<String, Object>[] choicesArray = new Map[choicesList.size()];
        for (int i = 0; i < choicesList.size(); i++) {
            Map<String, Object> choiceMap = new HashMap<>();
            choiceMap.put("value", choicesList.get(i).getValue());
            choiceMap.put("position", choicesList.get(i).getPosition());
            choicesArray[i] = choiceMap;
        }

        fieldMap.put("choices", choicesArray);

        return fieldMap;
    }

    public static Map<String, Object> createNewTicketFieldCustomText() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("customers_can_edit", true);
        fieldMap.put("label_for_customers", "Label for customer test");
        fieldMap.put("displayed_to_customers", true);
        fieldMap.put("label", "Label test");
        fieldMap.put("type", "custom_text");
        return fieldMap;
    }

    public static TicketForm createNewTicketForm() {
        Map<String, Object> field1 = new HashMap<>();
        field1.put("id", 103000803251L);
        field1.put("label_for_customers", "requester");
        field1.put("customers_can_edit", true);
        field1.put("required_for_customers", true);
        field1.put("placeholder_for_customers", "requester");
        field1.put("hint_for_customers", "requester");

        Map<String, Object> field2 = new HashMap<>();
        field2.put("id", 103000803261L);
        field2.put("label_for_customers", "company");
        field2.put("customers_can_edit", true);
        field2.put("required_for_customers", true);
        field2.put("placeholder_for_customers", "company");
        field2.put("hint_for_customers", "company");

        Map<String, Object> field3 = new HashMap<>();
        field3.put("id", 103000803252L);
        field3.put("label_for_customers", "subject");
        field3.put("customers_can_edit", true);
        field3.put("required_for_customers", true);
        field3.put("placeholder_for_customers", "subject");
        field3.put("hint_for_customers", "subject");

        Map<String, Object> field4 = new HashMap<>();
        field4.put("id", 103000803260L);
        field4.put("label_for_customers", "description");
        field4.put("customers_can_edit", true);
        field4.put("required_for_customers", true);
        field4.put("placeholder_for_customers", "description");
        field4.put("hint_for_customers", "description");

        Map<String, TicketField>[] fields = new Map[] {field1, field2, field3, field4};

        return new TicketForm("Custom ticket for testing", "This is a custom ticket form", fields);
    }

//    generating random string generates RestClientException:
//    Error while extracting response for type [class freshdesk.epharma.model.TicketForm.TicketForm]
//    and content type [application/json;charset=utf-8]
//    public class randomStringGenerator {
//        public static String generate() {
//            return UUID.randomUUID().toString();
//        }
//    }


}
