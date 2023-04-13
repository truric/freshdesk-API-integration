package freshdesk.epharma.model.TicketWatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class TicketWatcher {
    @JsonProperty("id")
    @NotNull
    private List<Integer> ids = new ArrayList<>();

    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("watcher_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> watcherIds = new ArrayList<>();
}
