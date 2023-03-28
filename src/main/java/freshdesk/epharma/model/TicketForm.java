package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketForm {

    public TicketForm(String title, String description, List<TicketFields> fields) {
        this.title = title;
        this.description = description;
        this.fields = fields;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @NotNull
    @JsonProperty("title")
    private String title;

    @NotNull
    @JsonProperty("default")
    private boolean isDefault;

    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("portals")
    private List<Object> portals;

    @NotNull
    @JsonProperty("fields")
    private List<TicketFields> fields;

    @NotNull
    @JsonProperty("last_updated_by")
    private LocalDate lastUpdatedBy;

}
