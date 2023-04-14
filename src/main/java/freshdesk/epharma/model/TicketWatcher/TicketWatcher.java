package freshdesk.epharma.model.TicketWatcher;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketWatcher {
    @JsonProperty("ids")
    @NotNull
    private List<Integer> ids = new ArrayList<>();

    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    public TicketWatcher(List<Integer> ids, Long user_id) {
        this.ids = ids;
        this.userId = user_id;
    }
}
