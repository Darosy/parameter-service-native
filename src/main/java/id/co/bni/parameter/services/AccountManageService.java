package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.AccountDetailRequest;
import id.co.bni.parameter.dto.request.AccountManagementRequest;
import id.co.bni.parameter.entity.AccountManagement;
import id.co.bni.parameter.entity.AccountManagementDetail;
import id.co.bni.parameter.repository.AccountManageDetailRepo;
import id.co.bni.parameter.repository.AccountManageRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountManageService {
    private final AccountManageRepo accountManageRepo;
    private final AccountManageDetailRepo accountManageDetailRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseEntity<ResponseService> create(AccountManagementRequest req) {
        if (accountManageRepo.findById(req.getCompanyId()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.BAD_REQUEST);

        if (req.getListAccount().isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.setResponseError("12", "List account cannot be empty", null, ""), HttpStatus.BAD_REQUEST);
        }

        AccountManagement accountManagement = AccountManagement.builder()
                .companyId(req.getCompanyId())
                .companyName(req.getCompanyName())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        accountManageRepo.save(accountManagement);

        req.getListAccount().forEach(data -> accountManageDetailRepo.save(AccountManagementDetail.builder()
                .companyId(req.getCompanyId())
                .dbAccount(data.getDbAccount())
                .dbAccountName(data.getDbAccountName())
                .build()));

        loadCache(accountManagement, req.getListAccount());
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, accountManagement, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(AccountManagementRequest req) {
        AccountManagement management = accountManageRepo.findByCompanyId(req.getCompanyId());
        if (management == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        if (req.getListAccount().isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.setResponseError("12", "List account cannot be empty", null, ""), HttpStatus.BAD_REQUEST);
        }

        management.setCompanyName(req.getCompanyName());
        management.setUpdatedAt(new Date());
        accountManageRepo.saveAndFlush(management);

        List<AccountManagementDetail> listDet = accountManageDetailRepo.findByCompanyId(req.getCompanyId());
        if (listDet != null && !listDet.isEmpty()) accountManageDetailRepo.deleteAll(listDet);

        req.getListAccount().forEach(data -> accountManageDetailRepo.save(AccountManagementDetail.builder()
                .companyId(req.getCompanyId())
                .dbAccount(data.getDbAccount())
                .dbAccountName(data.getDbAccountName())
                .build()));

        loadCache(management, req.getListAccount());
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, management, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(String companyId) {
        AccountManagement management = accountManageRepo.findByCompanyId(companyId);
        if (management == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        accountManageRepo.delete(management);

        List<AccountManagementDetail> listDet = accountManageDetailRepo.findByCompanyId(companyId);
        if (listDet != null && !listDet.isEmpty()) accountManageDetailRepo.deleteAll(listDet);

        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue(), companyId), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByCompanyId(String companyId) {
        AccountManagementRequest accountManagementRequest = parameterLoader.getAccountParam(companyId);
        if (accountManagementRequest == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "companyId :" + companyId + " Not Found"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, accountManagementRequest, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<AccountManagementRequest> collection = parameterLoader.getAllAccountParam();
        if (collection.isEmpty())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(AccountManagement accountManagement, List<AccountDetailRequest> listAccount) {
        ConcurrentHashMap<String, AccountManagementRequest> hAccountManagement = new ConcurrentHashMap<>();
        hAccountManagement.put(accountManagement.getCompanyId(), accountManagement.toAccountManagementResponse(listAccount));
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue(), accountManagement.getCompanyId(), hAccountManagement);
    }
}
