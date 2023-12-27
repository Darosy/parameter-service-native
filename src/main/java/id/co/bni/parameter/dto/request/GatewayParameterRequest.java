package id.co.bni.parameter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GatewayParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    @Size(max = 20)
    private String transCode;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String systemIdOrMcpId;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String paymentType;
    @NotNull
    @NotBlank
    @Size(max = 200)
    private String url;
    @NotNull
    private Boolean isUsingProxy;
    private String proxyIp;
    private String proxyPort;
}
