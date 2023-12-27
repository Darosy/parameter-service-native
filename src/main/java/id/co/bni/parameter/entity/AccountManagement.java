package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.AccountDetailRequest;
import id.co.bni.parameter.dto.request.AccountManagementRequest;
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
@Entity(name = "ACCOUNT_MANAGEMENT")
public class AccountManagement {
    @Id
    @Column(name = "COMPANY_ID", nullable = false, length = 50)
    private String companyId;

    @Column(name = "COMPANY_NAME", nullable = false, length = 100)
    private String companyName;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public AccountManagementRequest toAccountManagementResponse(List<AccountDetailRequest> listAccount) {
        return AccountManagementRequest.builder()
                .companyId(companyId)
                .companyName(companyName)
                .listAccount(listAccount)
                .build();
    }
}
