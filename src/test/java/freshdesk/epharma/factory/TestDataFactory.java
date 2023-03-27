package freshdesk.epharma.factory;

import freshdesk.epharma.model.Ticket;
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
}
