package freshdesk.epharma.factory;

import freshdesk.epharma.model.Ticket;
import freshdesk.epharma.model.TicketAttachment;
import freshdesk.epharma.model.TicketFields;
import freshdesk.epharma.model.TicketForm;

import java.util.*;

public class TestDataFactory {
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

    public static TicketForm createNewTicketForm() {
        List<TicketFields> fields = new ArrayList<>();

        TicketFields field1 = new TicketFields();
        field1.setId(1L);
        field1.setLabelForCustomer("Field 1");
        field1.setCustomerCanEdit(true);
        field1.setRequiredForCustomers(true);
        field1.setHintForCustomers("Hint for Field 1");
        field1.setPlaceholderForCustomers("Placeholder for Field 1");

        fields.add(field1);

        TicketFields field2 = new TicketFields();
        field2.setId(2L);
        field2.setLabelForCustomer("Field 2");
        field2.setCustomerCanEdit(true);
        field2.setRequiredForCustomers(true);
        field2.setHintForCustomers("Hint for Field 2");
        field2.setPlaceholderForCustomers("Placeholder for Field 2");

        fields.add(field2);

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

    public static TicketAttachment createNewTicketAttachment() {
        return new TicketAttachment(
                "image/png",
                "screenshot.png",
                Base64.getEncoder().encodeToString("Test attachment content".getBytes()).getBytes()
        );
    }
}
