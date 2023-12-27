package id.co.bni.parameter.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class McpParameterDetailResponse implements Serializable {
    @NotNull
    @NotBlank
    private String trxField;
    @NotNull
    @NotBlank
    private String match;
    @NotNull
    @NotBlank
    private String position;
    @NotNull
    @NotBlank
    private String billerCode;
    @NotNull
    @NotBlank
    private String billerName;
    @NotNull
    @NotBlank
    private String regionCode;
}
