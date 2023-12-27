package id.co.bni.parameter.cache;

import id.co.bni.parameter.dto.request.*;
import id.co.bni.parameter.dto.response.McpParameterDetailResponse;
import id.co.bni.parameter.dto.response.McpParameterFeeResponse;
import id.co.bni.parameter.entity.AccountManagementDetail;
import id.co.bni.parameter.entity.McpParameterDetail;
import id.co.bni.parameter.entity.McpParameterFee;
import id.co.bni.parameter.repository.*;
import id.co.bni.parameter.util.RestConstants;
import id.co.bni.parameter.util.RestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParameterLoader {
    private final GatewayParameterChannelRepo parameterChannelRepository;
    private final McpParameterRepo mcpParameterRepo;
    private final McpParameterFeeRepo mcpParameterFeeRepo;
    private final ChannelParameterRepo channelParameterRepo;
    private final McpParameterDetailRepo mcpParameterDetailRepo;
    private final KeyParameterRepo keyParameterRepo;
    private final AccountManageRepo accountManageRepo;
    private final AccountManageDetailRepo accountManageDetailRepo;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public void loadBackground() {
        long start = System.currentTimeMillis();
        load();
        log.info("Elapsed time = " + (System.currentTimeMillis() - start) + "ms");
    }

    @Async
    public void load() {
        loadGatewayParameter();
        loadMcpParameter();
        loadChannelParameter();
        loadKeyParameter();
        loadAccountParameter();
    }

    private void loadGatewayParameter() {
        ConcurrentHashMap<String, GatewayParameterRequest> hGatewayParameter = new ConcurrentHashMap<>();
        try {
            // id = transCode + systemIdOrMcpId + paymentType
            parameterChannelRepository.findAll()
                    .forEach(gatewayParam -> {
                        String id = gatewayParam.getTransCode() + gatewayParam.getSystemIdOrMcpId() + (gatewayParam.getPaymentType() != null ? gatewayParam.getPaymentType() : "-");
                        hGatewayParameter.put(id, gatewayParam.toGatewayParameterResponse());
                    });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.GATEWAY_PARAMETER.getValue(), hGatewayParameter);
    }

    private void loadMcpParameter() {
        ConcurrentHashMap<String, McpParameterRequest> hMcpParameter = new ConcurrentHashMap<>();
        try {
            mcpParameterRepo.findAll()
                    .forEach(mcpParameter ->
                            {
                                List<McpParameterDetailResponse> listDet = null;
                                List<McpParameterDetail> listDataDet = mcpParameterDetailRepo.findByMcpId(mcpParameter.getMcpId());
                                if (listDataDet != null && !listDataDet.isEmpty()) {
                                    listDet = new ArrayList<>();
                                    for (McpParameterDetail a : listDataDet) {
                                        listDet.add(McpParameterDetailResponse.builder()
                                                .trxField(a.getTrxField())
                                                .match(a.getMatchRegex())
                                                .position(a.getPosition())
                                                .billerCode(a.getBillerCode())
                                                .billerName(a.getBillerName())
                                                .regionCode(a.getRegionCode())
                                                .build());
                                    }
                                }

                                List<McpParameterFeeResponse> listFee = null;
                                List<McpParameterFee> listDataFee = mcpParameterFeeRepo.findByMcpId(mcpParameter.getMcpId());
                                if (listDataFee != null && !listDataFee.isEmpty()) {
                                    listFee = new ArrayList<>();
                                    for (McpParameterFee a : listDataFee) {
                                        listFee.add(McpParameterFeeResponse.builder()
                                                .currency(a.getCurrency())
                                                .fee(RestUtil.df.format(a.getFee().doubleValue()))
                                                .build());
                                    }
                                }
                                hMcpParameter.put(mcpParameter.getMcpId(), mcpParameter.toMcpParameterResponse(listFee, listDet));
                            }
                    );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue(), hMcpParameter);
    }

    private void loadChannelParameter() {
        ConcurrentHashMap<String, ChannelParameterRequest> hChannelParameter = new ConcurrentHashMap<>();
        try {
            // id = channelId + systemId
            channelParameterRepo.findAll()
                    .forEach(channelParameter -> hChannelParameter.put(channelParameter.getChannelId() + channelParameter.getSystemId(), channelParameter.toChannelParameterResponse()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue(), hChannelParameter);
    }

    private void loadKeyParameter() {
        ConcurrentHashMap<String, KeyParameterRequest> hKeyParameter = new ConcurrentHashMap<>();
        try {
            keyParameterRepo.findAll()
                    .forEach(keyParameter -> hKeyParameter.put(keyParameter.getKey(), keyParameter.toKeyParameterResponse()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue(), hKeyParameter);
    }

    private void loadAccountParameter() {
        ConcurrentHashMap<String, AccountManagementRequest> hAccountParameter = new ConcurrentHashMap<>();
        try {
            accountManageRepo.findAll()
                    .forEach(account ->
                            {
                                List<AccountDetailRequest> listDet = null;
                                List<AccountManagementDetail> listAccountDet = accountManageDetailRepo.findByCompanyId(account.getCompanyId());
                                if (listAccountDet != null && !listAccountDet.isEmpty()) {
                                    listDet = new ArrayList<>();
                                    for (AccountManagementDetail a : listAccountDet) {
                                        listDet.add(AccountDetailRequest.builder()
                                                .dbAccount(a.getDbAccount())
                                                .dbAccountName(a.getDbAccountName())
                                                .build());
                                    }
                                }
                                hAccountParameter.put(account.getCompanyId(), account.toAccountManagementResponse(listDet));
                            }
                    );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        clearPackHazelcast(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue(), hAccountParameter);
    }

    private void clearPackHazelcast(String cacheName, Map<String, ?> map) {
        redisTemplate.delete(cacheName);
        redisTemplate.opsForHash().putAll(cacheName, map);
    }

    @Async
    public void clearAndPut(String cacheName, String key, Map<String, ?> map) {
        if (redisTemplate.opsForHash().get(cacheName, key) != null)
            redisTemplate.opsForHash().delete(cacheName, key);
        redisTemplate.opsForHash().putAll(cacheName, map);
    }

    public Collection<GatewayParameterRequest> getAllGatewayParam() {
        Map<String, GatewayParameterRequest> h = (Map<String, GatewayParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.GATEWAY_PARAMETER.getValue());
        return h.values();
    }

    public GatewayParameterRequest getGatewayParam(String transCode) {
        Map<String, GatewayParameterRequest> h = (Map<String, GatewayParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.GATEWAY_PARAMETER.getValue());
        return h.get(transCode);
    }


    public Collection<McpParameterRequest> getAllMcpParam() {
        Map<String, McpParameterRequest> h = (Map<String, McpParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue());
        return h.values();
    }

    public McpParameterRequest getMcpParam(String mcpId) {
        Map<String, McpParameterRequest> h = (Map<String, McpParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue());
        return h.get(mcpId);
    }

    public Collection<ChannelParameterRequest> getAllChannelParam() {
        Map<String, ChannelParameterRequest> h = (Map<String, ChannelParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue());
        return h.values();
    }

    public ChannelParameterRequest getChannelParam(String channelIdAndSystemId) {
        Map<String, ChannelParameterRequest> h = (Map<String, ChannelParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue());
        return h.get(channelIdAndSystemId);
    }

    public Collection<KeyParameterRequest> getAllKeyParam() {
        Map<String, KeyParameterRequest> h = (Map<String, KeyParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue());
        return h.values();
    }

    public KeyParameterRequest getKeyParam(String key) {
        Map<String, KeyParameterRequest> h = (Map<String, KeyParameterRequest>) checkAndGet(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue());
        return h.get(key);
    }

    public Collection<AccountManagementRequest> getAllAccountParam() {
        Map<String, AccountManagementRequest> h = (Map<String, AccountManagementRequest>) checkAndGet(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue());
        return h.values();
    }

    public AccountManagementRequest getAccountParam(String key) {
        Map<String, AccountManagementRequest> h = (Map<String, AccountManagementRequest>) checkAndGet(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue());
        return h.get(key);
    }

    private Map<?, ?> checkAndGet(String cacheName) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(cacheName);
        if (map.isEmpty()) {
            load();
            map = redisTemplate.opsForHash().entries(cacheName);
        }
        return map;
    }

    public void deleteHash(String name, String key) {
        log.info("Reload with name: {} and key: {}", name, key);
        if (name.equals(RestConstants.CACHE_NAME.GATEWAY_PARAMETER.getValue())) {
            loadGatewayParameter();
        } else if (name.equals(RestConstants.CACHE_NAME.MCP_PARAMETER.getValue())) {
            loadMcpParameter();
        } else if (name.equals(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue())) {
            loadChannelParameter();
        } else if (name.equals(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue())) {
            loadKeyParameter();
        } else if (name.equals(RestConstants.CACHE_NAME.ACCOUNT_MANAGEMENT.getValue())) {
            loadAccountParameter();
        } else {
            load();
        }
    }
}
