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

    public static List<TicketField> createMandatoryTicketFields() {
        TicketField field1 = new TicketField();
        field1.setId(103000803251L);
        field1.setLabelForCustomers("requester");
        field1.setIsCustomersCanEdit(true);
        field1.setIsRequiredForCustomers(true);
        field1.setPlaceholderForCustomers("requester");
        field1.setHintForCustomers("requester");

        TicketField field2 = new TicketField();
        field2.setId(103000803261L);
        field2.setLabelForCustomers( "company");
        field2.setIsCustomersCanEdit(true);
        field2.setIsRequiredForCustomers(true);
        field2.setPlaceholderForCustomers("company");
        field2.setHintForCustomers("company");

        TicketField field3 = new TicketField();
        field3.setId(103000803252L);
        field3.setLabelForCustomers("subject");
        field3.setIsCustomersCanEdit(true);
        field3.setIsRequiredForCustomers(true);
        field3.setPlaceholderForCustomers("subject");
        field3.setHintForCustomers("subject");

        TicketField field4 = new TicketField();
        field4.setId(103000803260L);
        field4.setLabelForCustomers("description");
        field4.setIsCustomersCanEdit(true);
        field4.setIsRequiredForCustomers(true);
        field4.setPlaceholderForCustomers("description");
        field4.setHintForCustomers("description");

        List<TicketField> fields = new ArrayList<>();
        fields.add(field1);
        fields.add(field2);
        fields.add(field3);
        fields.add(field4);

        return fields;
    }

    public static TicketForm createNewTicketForm() {
        TicketForm ticketForm = new TicketForm();
        ticketForm.setTitle(randomStringGenerator.generate());
        ticketForm.setDescription("This is a custom ticket form");
        ticketForm.setFields(createMandatoryTicketFields());

        return ticketForm;
    }

    public static TicketForm createNewTicketForm2() {
        TicketField field1 = new TicketField();
        field1.setLabelForCustomers("tester");
        field1.setIsCustomersCanEdit(true);
        field1.setIsRequiredForCustomers(true);
        field1.setPlaceholderForCustomers("tester");
        field1.setHintForCustomers("tester");

        List<TicketField> fields = new ArrayList<>();
        fields.add(field1);

        return new TicketForm(randomStringGenerator.generate(), "This is a custom ticket form 2", fields);
    }

    public static TicketField createNewTicketField() {
        TicketField ticketField = new TicketField();
        ticketField.setLabel("Issue Type");
        ticketField.setIsCustomersCanEdit(true);
        ticketField.setIsRequiredForCustomers(true);
        ticketField.setHintForCustomers("New Hint For Customers");
        ticketField.setPlaceholderForCustomers("New Placeholder For Customers");
//  position is not mandatory field
//      ticketField.setPosition(1);
        return ticketField;
    }

//    generating random string generates RestClientException:
//    Error while extracting response for type [class freshdesk.epharma.model.TicketForm.TicketForm]
//    and content type [application/json;charset=utf-8]
    public class randomStringGenerator {
        public static String generate() {
            return UUID.randomUUID().toString();
        }
    }
}
