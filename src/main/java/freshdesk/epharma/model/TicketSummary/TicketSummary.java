package freshdesk.epharma.model.TicketSummary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import freshdesk.epharma.model.Ticket.TicketAttachment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSummary {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("body")
    @NotNull
    private String body;

    @JsonProperty("body_text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bodyText;

    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    @JsonProperty("ticket_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long ticketId;

    @JsonProperty("attachments")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketAttachment> attachments;

    @JsonProperty("cloud_files")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketAttachment> cloudFiles;

    @JsonProperty("last_edited_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastEditedAt;

    @JsonProperty("last_edited_user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastEditedUserId;

    public TicketSummary(String body) {
        this.body = body;
    }
}
