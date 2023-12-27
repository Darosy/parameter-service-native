package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.ChannelParameterRequest;
import id.co.bni.parameter.entity.ChannelParameter;
import id.co.bni.parameter.entity.ChannelParameterId;
import id.co.bni.parameter.repository.ChannelParameterRepo;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelParameterService {
    private final ChannelParameterRepo channelParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseEntity<ResponseService> create(ChannelParameterRequest req) {
        if (channelParameterRepo.findById(ChannelParameterId.builder()
                .channelId(req.getChannelId())
                .systemId(req.getSystemId())
                .build()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.BAD_REQUEST);

        ChannelParameter channelParameter = ChannelParameter.builder()
                .channelId(req.getChannelId())
                .systemId(req.getSystemId())
                .branch(req.getBranch())
                .teller(req.getTeller())
                .overrideFlag(req.getOverrideFlag())
                .tandemFlag(req.getTandemFlag())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        channelParameterRepo.save(channelParameter);
        loadCache(channelParameter);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channelParameter, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(ChannelParameterRequest req) {
        ChannelParameter channel = channelParameterRepo.findByChannelIdAndSystemId(req.getChannelId(), req.getSystemId());
        if (channel == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        channel.setBranch(req.getBranch());
        channel.setTeller(req.getTeller());
        channel.setOverrideFlag(req.getOverrideFlag());
        channel.setTandemFlag(req.getTandemFlag());
        channel.setUpdatedAt(new Date());
        channelParameterRepo.saveAndFlush(channel);
        loadCache(channel);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channel, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(String channelId, String systemId) {
        ChannelParameter channel = channelParameterRepo.findByChannelIdAndSystemId(channelId, systemId);
        if (channel == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        channelParameterRepo.delete(channel);
        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue(), channelId + systemId), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByChannelIdAndSystemId(String channelId, String systemId) {
        ChannelParameterRequest channelParameterRequest = parameterLoader.getChannelParam(channelId + systemId);
        if (channelParameterRequest == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "channelId and systemId : " + channelId + "-" + systemId + " Not Found"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, channelParameterRequest, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<ChannelParameterRequest> collection = parameterLoader.getAllChannelParam();
        if (collection.isEmpty())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(ChannelParameter channelParameter) {
        ConcurrentHashMap<String, ChannelParameterRequest> hChannelParameter = new ConcurrentHashMap<>();
        hChannelParameter.put(channelParameter.getChannelId() + channelParameter.getSystemId(), channelParameter.toChannelParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.CHANNEL_PARAMETER.getValue(), channelParameter.getChannelId() + channelParameter.getSystemId(), hChannelParameter);
    }
}
