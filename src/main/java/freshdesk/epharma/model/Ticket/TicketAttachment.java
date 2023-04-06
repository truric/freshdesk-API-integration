package freshdesk.epharma.model.Ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketAttachment implements Serializable {
    @JsonProperty("name")
    private String name;

    @JsonProperty("contentType")
    private String contentType;

    @JsonProperty("data")
    private byte[] data;
}
