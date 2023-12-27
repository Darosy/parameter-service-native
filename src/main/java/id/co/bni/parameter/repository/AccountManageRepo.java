package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.AccountManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountManageRepo extends JpaRepository<AccountManagement, String> {
    @Query("select km from ACCOUNT_MANAGEMENT km where km.companyId = ?1")
    AccountManagement findByCompanyId(String companyId);
}
