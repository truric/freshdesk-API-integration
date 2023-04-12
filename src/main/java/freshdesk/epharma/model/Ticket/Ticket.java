package freshdesk.epharma.model.Ticket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Ticket {

    public Ticket(String phone, Integer source, Integer status, Integer priority, String subject,
                  String description, String name) {
        this.phone = phone;
        this.source = source;
        this.status = status;
        this.priority = priority;
        this.subject = subject;
        this.description = description;
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @NotNull
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("source")
    private Integer source;

    @NotNull
    @JsonProperty("status")
    private Integer status;

    @NotNull
    @JsonProperty("priority")
    private Integer priority;

    @NotNull
    @JsonProperty("subject")
    private String subject;

    @NotNull
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("requester_id")
    private Long requesterId;

    @JsonProperty("attachments")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketAttachment> attachments;

}