package freshdesk.epharma.model.TicketFields;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TicketFieldChoices {

    @JsonProperty("value")
    @NotNull
    private String value;

    @JsonProperty("position")
    @NotNull
    private Integer position;

}