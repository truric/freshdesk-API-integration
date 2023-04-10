package freshdesk.epharma.model.TicketFields;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFieldSection {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("label")
    @NotNull
    private String label;

    @JsonProperty("parent_ticket_field_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentTicketFieldId;


    @JsonProperty("ticket_field_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> ticketFieldIds;

    @JsonProperty("is_fsm")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isFsm;

    @JsonProperty("choice_ids")
    @NotNull
    private Long choices;

    public TicketFieldSection(String label, Long choices) {
        this.label = label;
        this.choices = choices;
    }
}
