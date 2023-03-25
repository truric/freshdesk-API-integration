package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
public class Ticket {

    public Ticket(String phone, int source, int status, int priority, String subject,
                  String description, String name) {
        this.phone = phone;
        this.source = source;
        this.status = status;
        this.priority = priority;
        this.subject = subject;
        this.description = description;
        this.name = name;
    }

    private Long id;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("name")
    private String name;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("requester_id")
    private Long requesterId;

    @JsonProperty("responder_id")
    private Long responderId;
    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("cc_emails")
    private List<String> ccEmails;

    @JsonProperty("fwd_emails")
    private List<String> fwdEmails;

    @JsonProperty("reply_cc_emails")
    private List<String> replyCcEmails;

    @JsonProperty("source")
    private Integer source;

    @JsonProperty("email_config_id")
    private Long emailConfigId;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("company_id")
    private Long companyId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("to_emails")
    private List<String> toEmails;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("fr_escalated")
    private Boolean frEscalated;

    @JsonProperty("spam")
    private Boolean spam;

    @JsonProperty("urgent")
    private Boolean urgent;

    @JsonProperty("is_escalated")
    private Boolean isEscalated;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("due_by")
    private LocalDateTime dueBy;

    @JsonProperty("fr_due_by")
    private LocalDateTime frDueBy;

    @JsonProperty("description_text")
    private String descriptionText;

    @JsonProperty("description")
    private String description;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("attachments")
    private List<String> attachments;
}