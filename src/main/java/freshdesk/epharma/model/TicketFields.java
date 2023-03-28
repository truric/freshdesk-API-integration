package freshdesk.epharma.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketFields {
    @NotNull
    @JsonProperty("id")
    private Long id;
    @NotNull
    @JsonProperty("label_for_customers")
    private String labelForCustomer;
    @NotNull
    @JsonProperty("customers_can_edit")
    private boolean customerCanEdit;
    @NotNull
    @JsonProperty("required_for_customers")
    private boolean requiredForCustomers;
    @NotNull
    @JsonProperty("hint_for_customers")
    private String hintForCustomers;
    @NotNull
    @JsonProperty("placeholder_for_customers")
    private String placeholderForCustomers;
    @NotNull
    @JsonProperty("position")
    private Integer position;
}
