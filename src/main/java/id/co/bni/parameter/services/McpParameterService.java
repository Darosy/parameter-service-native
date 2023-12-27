package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.McpParameterRequest;
import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import id.co.bni.parameter.entity.McpParameter;
import id.co.bni.parameter.entity.McpParameterDetail;
import id.co.bni.parameter.entity.McpParameterFee;
import id.co.bni.parameter.repository.McpParameterDetailRepo;
import id.co.bni.parameter.repository.McpParameterFeeRepo;
import id.co.bni.parameter.repository.McpParameterRepo;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class McpParameterService {
    private final McpParameterRepo mcpParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;
    private final McpParameterFeeRepo mcpParameterFeeRepo;
    private final McpParameterDetailRepo mcpParameterDetailRepo;

    @Transactional
    public ResponseEntity<ResponseService> create(McpParameterRequest req) {
        if (mcpParameterRepo.findById(req.getMcpId()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.BAD_REQUEST);

        if (!req.getIsMatch() && req.getDetail().size() > 1) {
            return new ResponseEntity<>(ResponseUtil.setResponseError("12", "Detail data cannot be more than 1 ", null, ""), HttpStatus.BAD_REQUEST);
        }

        McpParameter mcpParameter = McpParameter.builder()
                .mcpId(req.getMcpId())
                .isMatch(req.getIsMatch())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        mcpParameterRepo.save(mcpParameter);

        req.getDetail().forEach(detail -> mcpParameterDetailRepo.save(McpParameterDetail.builder()
                .mcpId(mcpParameter.getMcpId())
                        .trxField(detail.getTrxField())
                        .matchRegex(detail.getMatch())
                        .position(detail.getPosition())
                        .billerCode(detail.getBillerCode())
                        .billerName(detail.getBillerName())
                        .regionCode(detail.getRegionCode())
                .build()));

        req.getDataFee().forEach(fee -> mcpParameterFeeRepo.save(McpParameterFee.builder()
                .mcpId(mcpParameter.getMcpId())
                .currency(fee.getCurrency())
                .fee(BigDecimal.valueOf(Double.parseDouble(fee.getFee())))
                .build()));
        loadCache(mcpParameter, req.getDataFee(), req.getDetail());
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(McpParameterRequest req) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(req.getMcpId());
        if (mcpParameter == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        if (!req.getIsMatch() && req.getDetail().size() > 1) {
            return new ResponseEntity<>(ResponseUtil.setResponseError("12", "Detail data cannot be more than 1 ", null, ""), HttpStatus.BAD_REQUEST);
        }

        mcpParameter.setIsMatch(req.getIsMatch());
        mcpParameter.setUpdatedAt(new Date());
        mcpParameterRepo.saveAndFlush(mcpParameter);

        List<McpParameterDetail> listDet = mcpParameterDetailRepo.findByMcpId(req.getMcpId());
        if (listDet != null && !listDet.isEmpty()) mcpParameterDetailRepo.deleteAll(listDet);

        List<McpParameterFee> listFee = mcpParameterFeeRepo.findByMcpId(req.getMcpId());
        if (listFee != null && !listFee.isEmpty()) mcpParameterFeeRepo.deleteAll(listFee);

        req.getDataFee().forEach(fee -> mcpParameterFeeRepo.save(McpParameterFee.builder()
                .mcpId(mcpParameter.getMcpId())
                .currency(fee.getCurrency())
                .fee(BigDecimal.valueOf(Double.parseDouble(fee.getFee())))
                .build()));

        req.getDetail().forEach(detail -> mcpParameterDetailRepo.save(McpParameterDetail.builder()
                .mcpId(mcpParameter.getMcpId())
                .trxField(detail.getTrxField())
                .matchRegex(detail.getMatch())
                .position(detail.getPosition())
                .billerCode(detail.getBillerCode())
                .billerName(detail.getBillerName())
                .regionCode(detail.getRegionCode())
                .build()));

        loadCache(mcpParameter, req.getDataFee(), req.getDetail());
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameter, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(String mcpId) {
        McpParameter mcpParameter = mcpParameterRepo.findByMcpId(mcpId);
        if (mcpParameter == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        mcpParameterRepo.delete(mcpParameter);

        List<McpParameterFee> listFee = mcpParameterFeeRepo.findByMcpId(mcpId);
        if (listFee != null && !listFee.isEmpty()) mcpParameterFeeRepo.deleteAll(listFee);

        List<McpParameterDetail> listDet = mcpParameterDetailRepo.findByMcpId(mcpId);
        if (listDet != null && !listDet.isEmpty()) mcpParameterDetailRepo.deleteAll(listDet);

        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue(), mcpId), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByMcpId(String mcpId) {
        McpParameterRequest mcpParameterRequest = parameterLoader.getMcpParam(mcpId);
        if (mcpParameterRequest == null) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, mcpParameterRequest, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<McpParameterRequest> collection = parameterLoader.getAllMcpParam();
        if (collection.isEmpty()) return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(McpParameter mcpParameter, List<McpParameterFeeResponse> listFee, List<McpParameterDetailResponse> listDet) {
        ConcurrentHashMap<String, McpParameterRequest> hMcpParameter = new ConcurrentHashMap<>();
        hMcpParameter.put(mcpParameter.getMcpId(), mcpParameter.toMcpParameterResponse(listFee, listDet));
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue(), mcpParameter.getMcpId(), hMcpParameter);
    }
}
