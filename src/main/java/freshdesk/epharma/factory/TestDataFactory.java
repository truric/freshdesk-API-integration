package freshdesk.epharma.factory;

import freshdesk.epharma.model.Ticket.Ticket;
import freshdesk.epharma.model.Ticket.TicketAttachment;
import freshdesk.epharma.model.TicketFields.TicketFieldChoices;
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

//    public static TicketForm createNewTicketForm() {
//        int numberOfFields = 2;
//        Map<String, TicketFields>[] fields = new HashMap[numberOfFields];
//
//        Map<String, TicketFields> field1 = new HashMap<>();
//        TicketFields ticketFields1 = new TicketFields();
//        ticketFields1.setCustomerCanEdit(true);
//        ticketFields1.setLabelForCustomer("Label for customers test 1");
//        ticketFields1.setDisplayedToCustomers(true);
//        ticketFields1.setLabel("Label test 1");
//        ticketFields1.setType("custom_text");
//        field1.put("field1", ticketFields1);
//        fields[0] = field1;
//
//        Map<String, TicketFields> field2 = new HashMap<>();
//        TicketFields ticketFields2 = new TicketFields();
//        ticketFields2.setCustomerCanEdit(true);
//        ticketFields2.setLabelForCustomer("Label for customers test 2");
//        ticketFields2.setDisplayedToCustomers(true);
//        ticketFields2.setLabel("Label test 2");
//        ticketFields2.setPosition(1);
//        ticketFields2.setType("custom_dropdown");
//
//
//        TicketFieldChoices[] choices = new TicketFieldChoices[]{
//                new TicketFieldChoices("Refund", 1),
//                new TicketFieldChoices("Faulty Product", 2),
//                new TicketFieldChoices("Item Not Delivered", 3)
//        };
//        Map<String, TicketFieldChoices>[] choicesArray = new Map[choices.length];
//        for (int i = 0; i < choices.length; i++) {
//            Map<String, TicketFieldChoices> choiceMap = new HashMap<>();
//            choiceMap.put("choice" + i, choices[i]);
//            choicesArray[i] = choiceMap;
//        }
//        ticketFields2.setChoices(choicesArray);
//
//        field2.put("field2", ticketFields2);
//        fields[1] = field2;
//
//        return new TicketForm("Ticket Form Title", "Ticket Form Description", fields);
//    }


//    public static TicketForm createUpdatedTicketForm() {
//        List<TicketFields> fields = new ArrayList<>();
//
//        TicketFields field3 = new TicketFields();
//        field3.setId(2L);
//        field3.setLabelForCustomer("Field 3");
//        field3.setCustomerCanEdit(false);
//        field3.setRequiredForCustomers(false);
//        field3.setHintForCustomers("Hint for Field 3");
//        field3.setPlaceholderForCustomers("Placeholder for Field 3");
//
//        fields.add(field3);
//
//        TicketFields field4 = new TicketFields();
//        field4.setId(3L);
//        field4.setLabelForCustomer("Field 4");
//        field4.setCustomerCanEdit(false);
//        field4.setRequiredForCustomers(false);
//        field4.setHintForCustomers("Hint for Field 4");
//        field4.setPlaceholderForCustomers("Placeholder for Field 4");
//
//        fields.add(field4);
//
//        return new TicketForm("Ticket Form Title", "Ticket Form Description", fields);
//    }

}
