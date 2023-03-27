package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TicketResponse {
    public TicketResponse(String subject, String description, String name, Long requesterId, Integer status, Integer priority) {
        this.subject = subject;
        this.description = description;
        this.name = name;
        this.requesterId = requesterId;
        this.status = status;
        this.priority = priority;
    }
    @JsonProperty("id")
    private Long id;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email_config_id")
    private Integer emailConfigId;

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("requester_id")
    private Long requesterId;

    @JsonProperty("responder_id")
    private Integer responderId;

    @JsonProperty("source")
    private Integer source;

    @JsonProperty("company_id")
    private Integer companyId;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("due_by")
    private String dueBy;

    @JsonProperty("fr_due_by")
    private String frDueBy;

    @JsonProperty("description")
    private String description;

    @JsonProperty("custom_fields")
    private Map<String, Object> customFields;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("attachments")
    private List<String> attachments;
}