package freshdesk.epharma.model.TicketUserAccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUserAccess {

    @JsonProperty("user_ids")
    @NotNull
    private List<Long> userIds = new ArrayList<>();

}
