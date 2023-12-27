package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.validation.annotation.NumberOnly;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChannelParameterRequest implements Serializable {
    @NotNull
    @NotBlank
    private String channelId;

    @NotNull
    @NotBlank
    private String systemId;

    @NotNull
    @NotBlank
    @NumberOnly
    private String branch;

    @NotNull
    @NotBlank
    @NumberOnly
    private String teller;

    @NotNull
    @NotBlank
    private String overrideFlag;

    @NotNull
    @NotBlank
    @NumberOnly
    private String tandemFlag;
}
