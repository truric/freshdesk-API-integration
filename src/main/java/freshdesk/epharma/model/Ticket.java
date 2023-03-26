package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
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

//    @JsonProperty("requester_id")
//    private Long requesterId;
//
//    @JsonProperty("responder_id")
//    private Long responderId;
//
//
//    @JsonProperty("cc_emails")
//    private List<String> ccEmails;
//
//    @JsonProperty("fwd_emails")
//    private List<String> fwdEmails;
//
//    @JsonProperty("reply_cc_emails")
//    private List<String> replyCcEmails;
//
//
//    @JsonProperty("email_config_id")
//    private Long emailConfigId;
//
//    @JsonProperty("group_id")
//    private Long groupId;
//
//    @JsonProperty("company_id")
//    private Long companyId;
//
//    @JsonProperty("type")
//    private String type;
//
//    @JsonProperty("to_emails")
//    private List<String> toEmails;
//
//    @JsonProperty("product_id")
//    private Long productId;
//
//    @JsonProperty("fr_escalated")
//    private Boolean frEscalated;
//
//    @JsonProperty("spam")
//    private Boolean spam;
//
//    @JsonProperty("urgent")
//    private Boolean urgent;
//
//    @JsonProperty("is_escalated")
//    private Boolean isEscalated;
//
//    @JsonProperty("created_at")
//    private LocalDateTime createdAt;
//
//    @JsonProperty("updated_at")
//    private LocalDateTime updatedAt;
//
//    @JsonProperty("due_by")
//    private LocalDateTime dueBy;
//
//    @JsonProperty("fr_due_by")
//    private LocalDateTime frDueBy;
//
//    @JsonProperty("description_text")
//    private String descriptionText;
//
//
//    @JsonProperty("tags")
//    private List<String> tags;
//
//    @JsonProperty("attachments")
//    private List<String> attachments;
}