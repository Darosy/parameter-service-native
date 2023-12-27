package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class McpParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    private String mcpId;
    @NotNull
    private Boolean isMatch;
    @Valid
    private List<McpParameterDetailResponse> detail;
    @Valid
    private List<McpParameterFeeResponse> dataFee;
}
