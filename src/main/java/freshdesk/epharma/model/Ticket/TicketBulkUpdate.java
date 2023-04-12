package freshdesk.epharma.model.Ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketBulkUpdate {
    @NotNull
    @JsonProperty("ids")
    private List<Integer> ids = new ArrayList<>();

    @JsonProperty("properties")
    private Map<String, Integer> properties = new HashMap<>();

    @JsonProperty("reply")
    private TicketReply ticketReply;
}
