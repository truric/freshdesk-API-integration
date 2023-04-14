package freshdesk.epharma.model.TicketFields;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFieldResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonProperty("description")
    private String description;

    @JsonProperty("position")
    private Integer position;

    @JsonProperty("required_for_closure")
    private Boolean requiredForClosure;

    @JsonProperty("required_for_agents")
    private Boolean requiredForAgents;

    @JsonProperty("type")
    private String type;

    @JsonProperty("default")
    private Boolean defaultField;

    @JsonProperty("customers_can_edit")
    private Boolean customersCanEdit;

    @JsonProperty("customers_can_filter")
    private Boolean customersCanFilter;

    @JsonProperty("label_for_customers")
    private String labelForCustomers;

    @JsonProperty("required_for_customers")
    private Boolean requiredForCustomers;

    @JsonProperty("displayed_to_customers")
    private Boolean displayedToCustomers;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("choices")
    private List<String> choices;
}
