package freshdesk.epharma.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TicketFieldTypeDeserializer extends JsonDeserializer<TicketFieldType> {

    @Override
    public TicketFieldType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String value = node.asText();
        return TicketFieldType.valueOf(value);
    }
}