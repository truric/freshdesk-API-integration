package freshdesk.epharma.model.TicketUserAccess;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private List<UserAccessPatch> userIds = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAccessPatch {
        @JsonProperty("id")
        @NotNull
        private Long id;

        @JsonProperty("deleted")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean deleted;
    }
}
