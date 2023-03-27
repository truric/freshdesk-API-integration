package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    @JsonProperty("phone")
    private String phone;
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
}