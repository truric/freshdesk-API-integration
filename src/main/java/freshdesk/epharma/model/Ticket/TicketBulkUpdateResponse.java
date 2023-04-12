package freshdesk.epharma.model.Ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBulkUpdateResponse {
    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("href")
    private String href;
}