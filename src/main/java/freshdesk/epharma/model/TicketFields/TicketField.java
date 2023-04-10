package freshdesk.epharma.model.TicketFields;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TicketField {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("label_for_customers")
    @NotNull
    private String labelForCustomers;

    @JsonProperty("customers_can_edit")
    @NotNull
    private boolean isCustomersCanEdit;

    @JsonProperty("displayed_to_customers")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isDisplayedToCustomers;

    @JsonProperty("label")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String label;

    @JsonProperty("type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonProperty("required_for_customers")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isRequiredForCustomers;

    @JsonProperty("placeholder_for_customers")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String placeholderForCustomers;

    @JsonProperty("hint_for_customers")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hintForCustomers;

    @JsonProperty("position")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 1, message = "Value must be between 1 and 17 inclusive")
    @Max(value = 17, message = "Value must be between 1 and 17 inclusive")
    private Integer position;

    @JsonProperty("choices")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TicketFieldChoices> choices;

    public TicketField(String labelForCustomers, boolean isCustomersCanEdit, boolean isDisplayedToCustomers, String label,
                       boolean isRequiredForCustomers, Integer position) {
        this.labelForCustomers = labelForCustomers;
        this.isCustomersCanEdit = isCustomersCanEdit;
        this.isDisplayedToCustomers = isDisplayedToCustomers;
        this.label = label;
        this.isRequiredForCustomers = isRequiredForCustomers;
        this.position = position;
    }
}
