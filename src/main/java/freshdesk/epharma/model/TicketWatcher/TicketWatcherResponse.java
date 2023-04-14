package freshdesk.epharma.model.TicketWatcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketWatcherResponse {

    @JsonProperty("watcher_ids")
    private List<Long> watcherIds = new ArrayList<>();
}
