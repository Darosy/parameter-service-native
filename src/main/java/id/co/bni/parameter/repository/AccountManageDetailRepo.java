package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.AccountManagementDetail;
import id.co.bni.parameter.entity.AccountManagementDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountManageDetailRepo extends JpaRepository<AccountManagementDetail, AccountManagementDetailId> {
    @Query("select km from ACCOUNT_MANAGEMENT_DETAIL km where km.companyId = ?1")
    List<AccountManagementDetail> findByCompanyId(String companyId);
}
