package freshdesk.epharma.model.TicketWatcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketWatcherBulkUnwatchRequest {
    private List<Integer> ids;
}
