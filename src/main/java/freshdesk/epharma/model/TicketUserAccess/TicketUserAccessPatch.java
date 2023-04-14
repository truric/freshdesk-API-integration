package freshdesk.epharma.model.TicketUserAccess;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketUserAccessPatch {

    @JsonProperty("user_ids")
    private List<UserAccessPatch> userIds = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAccessPatch {

        @JsonProperty("id")
        private Long id;
        @JsonProperty("deleted")
        private Boolean deleted;

    }
}
