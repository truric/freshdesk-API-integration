package freshdesk.epharma.model.Ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketQueryDTO {
    @JsonProperty("agent_id")
    private Integer agentId;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("type")
    private String type;

    @JsonProperty("due_by")
    private LocalDate dueBy;

    @JsonProperty("fr_due_by")
    private LocalDate frDueBy;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;
}