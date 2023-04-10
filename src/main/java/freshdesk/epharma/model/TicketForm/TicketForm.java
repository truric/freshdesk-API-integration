package freshdesk.epharma.model.TicketForm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import freshdesk.epharma.model.TicketFields.TicketField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TicketForm {

    public TicketForm(String title, String description, Map<String, TicketField>[] fields) {
        this.title = title;
        this.description = description;
        this.fields = fields;
    }

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    @UniqueElements
    private String name;

    @JsonProperty("title")
    @NotNull
    private String title;

    @JsonProperty("default")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isDefault;

    @JsonProperty("description")
    @NotNull
    private String description;

    @JsonProperty("portals")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> portals;

    @JsonProperty("fields")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, TicketField>[] fields;

    @JsonProperty("last_updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lastUpdatedBy;

}
