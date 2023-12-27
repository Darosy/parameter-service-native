package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "MCP_PARAMETER_DETAIL")
@IdClass(McpParameterDetailId.class)
public class McpParameterDetail {
    //    @Id
//    @GeneratedValue(generator = "system-uuid2")
//    @GenericGenerator(name = "system-uuid2", strategy = "uuid2")
//    private String id;
    @Id
    @Column(name = "MCP_ID", nullable = false, length = 50)
    private String mcpId;

    @Id
    @Column(name = "TRX_FIELD", nullable = false, length = 30)
    private String trxField;

    @Id
    @Column(name = "MATCH_REGEX", nullable = false, length = 20)
    private String matchRegex;

    @Column(name = "POSITION", nullable = false, length = 5)
    private String position;

    @Column(name = "BILLER_CODE", nullable = false, length = 10)
    private String billerCode;

    @Column(name = "BILLER_NAME", nullable = false, length = 100)
    private String billerName;

    @Column(name = "REGION_CODE", nullable = false, length = 10)
    private String regionCode;

    public McpParameterDetailResponse toMcpDetailResponse() {
        return McpParameterDetailResponse.builder()
                .trxField(trxField)
                .match(matchRegex)
                .position(position)
                .billerCode(billerCode)
                .billerName(billerName)
                .regionCode(regionCode)
                .build();
    }
}
