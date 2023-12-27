package id.co.bni.parameter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class McpParameterDetailId implements Serializable {
    private String mcpId;
    private String trxField;
    private String matchRegex;
}
