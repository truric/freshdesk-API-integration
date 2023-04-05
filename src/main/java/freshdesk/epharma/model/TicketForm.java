package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

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

//    @JsonProperty("default")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private boolean isDefault;

    @JsonProperty("description")
    @NotNull
    private String description;

    @JsonProperty("portals")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> portals;

    @JsonProperty("fields")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketFields> fields;

    @JsonProperty("last_updated_by")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lastUpdatedBy;

}
