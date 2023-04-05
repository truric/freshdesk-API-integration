package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketFieldChoices {
    @JsonProperty("value")
    @NotNull
    private String value;
    @JsonProperty("position")
    @NotNull
    private Integer position;

}