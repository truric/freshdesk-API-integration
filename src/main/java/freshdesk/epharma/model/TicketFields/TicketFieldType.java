package freshdesk.epharma.model.TicketFields;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketFieldType {
    CUSTOM_TEXT("custom_text"),
    CUSTOM_PARAGRAPH("custom_paragraph"),
    CUSTOM_CHECKBOX("custom_checkbox"),
    CUSTOM_NUMBER("custom_number"),
    CUSTOM_DATE("custom_date"),
    CUSTOM_DECIMAL("custom_decimal"),
    NESTED_FIELD("nested_field"),
    CUSTOM_DROPDOWN("custom_dropdown"),
    LOOKUP("lookup"),
    ENCRYPTED_TEXT("encrypted_text"),
    SECURE_TEXT("secure_text");

    private final String value;

    TicketFieldType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
