package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.McpParameterRequest;
import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "MCP_PARAMETER")
public class McpParameter {
    @Id
    @Column(name = "MCP_ID", nullable = false, length = 50)
    private String mcpId;

    private Boolean isMatch;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public McpParameterRequest toMcpParameterResponse(List<McpParameterFeeResponse> listFee, List<McpParameterDetailResponse> listDet) {
        return McpParameterRequest.builder()
                .mcpId(mcpId)
                .isMatch(isMatch)
                .detail(listDet)
                .dataFee(listFee)
                .build();
    }
}