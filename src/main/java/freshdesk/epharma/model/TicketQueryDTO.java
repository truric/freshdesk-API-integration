package freshdesk.epharma.model;

import lombok.Data;
@Data
public class TicketQueryDTO {
    private String query;
    private Long updatedSince;
    private String include;
    private Integer page;
    private Integer perPage;
    private String orderType;
    private String orderBy;
    private String requesterId;
    private String email;
    private String companyIds;
    private String groupIds;
    private String agentIds;
    private String teamIds;
    private String tags;
    private String filterName;
    private String filterDescription;
}
