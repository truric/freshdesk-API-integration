package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketFields {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("label_for_customers")
    @NotNull
    private String labelForCustomer;

    @JsonProperty("customers_can_edit")
    @NotNull
    private boolean customerCanEdit;

    @JsonProperty("displayed_to_customers")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean displayedToCustomers;

    @JsonProperty("label")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String label;

    @JsonProperty("type")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonDeserialize(using = TicketFieldTypeDeserializer.class)
    private TicketFieldType type;

    @JsonProperty("required_for_customers")
    @NotNull
    private boolean requiredForCustomers;

    @JsonProperty("placeholder_for_customers")
    @NotNull
    private String placeholderForCustomers;

    @JsonProperty("hint_for_customers")
    @NotNull
    private String hintForCustomers;

    @JsonProperty("position")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 1, message = "Value must be between 1 and 17 inclusive")
    @Max(value = 17, message = "Value must be between 1 and 17 inclusive")
    private Integer position;

    @JsonProperty("choices")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TicketFieldChoices[] choices;
}
