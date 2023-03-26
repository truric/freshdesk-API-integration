//package freshdesk.epharma.model;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.List;
//import java.util.Map;
//
//@Data
//public class TicketResponse {
//    @JsonProperty("id")
//    private Integer id;
//    @JsonProperty("cc_emails")
//    private List<String> ccEmails;
//    @JsonProperty("fwd_emails")
//    private List<String> fwdEmails;
//    @JsonProperty("reply_cc_emails")
//    private List<String> replyCcEmails;
//    @JsonProperty("ticket_cc_emails")
//    private List<String> ticketCcEmails;
//    @JsonProperty("fr_escalated")
//    private boolean frEscalated;
//    @JsonProperty("spam")
//    private boolean spam;
//    @JsonProperty("email_config_id")
//    private Integer emailConfigId;
//    @JsonProperty("group_id")
//    private Integer groupId;
//    @JsonProperty("priority")
//    private Integer priority;
//    @JsonProperty("requester_id")
//    private Integer requesterId;
//    @JsonProperty("responder_id")
//    private Integer responderId;
//    @JsonProperty("source")
//    private Integer source;
//    @JsonProperty("company_id")
//    private Integer companyId;
//    @JsonProperty("status")
//    private Integer status;
//    @JsonProperty("subject")
//    private String subject;
//    @JsonProperty("support_email")
//    private String supportEmail;
//    @JsonProperty("to_emails")
//    private String toEmails;
//    @JsonProperty("product_id")
//    private Integer productId;
//    @JsonProperty("type")
//    private String type;
//    @JsonProperty("due_by")
//    private String dueBy;
//    @JsonProperty("fr_due_by")
//    private String frDueBy;
//    @JsonProperty("is_escalated")
//    private boolean isEscalated;
//    @JsonProperty("description")
//    private String description;
//    @JsonProperty("description_text")
//    private String descriptionText;
//    @JsonProperty("custom_fields")
//    private Map<String, Object> customFields;
//    @JsonProperty("created_at")
//    private String createdAt;
//    @JsonProperty("updated_at")
//    private String updatedAt;
//    @JsonProperty("tags")
//    private List<String> tags;
//    @JsonProperty("attachments")
//    private List<String> attachments;
//    @JsonProperty("nr_due_by")
//    private String nrDueBy;
//    @JsonProperty("nr_escalated")
//    private boolean nrEscalated;
//}