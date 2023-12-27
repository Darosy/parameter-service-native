package id.co.bni.parameter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountManagementRequest implements Serializable {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String companyId;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String companyName;
    @Valid
    private List<AccountDetailRequest> listAccount;
}
