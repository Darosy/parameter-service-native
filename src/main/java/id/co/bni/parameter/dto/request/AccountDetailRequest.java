package id.co.bni.parameter.dto.request;

import id.co.bni.parameter.validation.annotation.NumberOnly;
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
public class AccountDetailRequest implements Serializable {
    @NotNull
    @NotBlank
    @NumberOnly
    @Size(max = 20)
    private String dbAccount;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String dbAccountName;
}
