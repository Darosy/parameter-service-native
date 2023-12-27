package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.KeyParameterRequest;
import id.co.bni.parameter.entity.KeyParameter;
import id.co.bni.parameter.repository.KeyParameterRepo;
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
public class KeyParameterService {
    private final KeyParameterRepo keyParameterRepo;
    private final ParameterLoader parameterLoader;
    private final CacheService cacheService;

    @Transactional
    public ResponseEntity<ResponseService> create(KeyParameterRequest req) {
        if (keyParameterRepo.findById(req.getKey()).isPresent())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_ALREADY_EXIST, null, ""), HttpStatus.BAD_REQUEST);

        KeyParameter keyParameter = KeyParameter.builder()
                .key(req.getKey())
                .value(req.getValue())
                .desc(req.getDesc())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        keyParameterRepo.save(keyParameter);
        loadCache(keyParameter);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParameter, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> update(KeyParameterRequest req) {
        KeyParameter keyParameter = keyParameterRepo.findByKey(req.getKey());
        if (keyParameter == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        keyParameter.setValue(req.getValue());
        keyParameter.setDesc(req.getDesc());
        keyParameter.setUpdatedAt(new Date());
        keyParameterRepo.saveAndFlush(keyParameter);
        loadCache(keyParameter);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParameter, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseService> delete(String key) {
        KeyParameter keyParameter = keyParameterRepo.findByKey(key);
        if (keyParameter == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);

        keyParameterRepo.delete(keyParameter);
        return new ResponseEntity<>(cacheService.reloadByKey(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue(), key), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findByKey(String key) {
        KeyParameterRequest keyParam = parameterLoader.getKeyParam(key);
        if (keyParam == null)
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, "key : " + key + " Not Found"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, keyParam, ""), HttpStatus.OK);
    }

    public ResponseEntity<ResponseService> findAll() {
        Collection<KeyParameterRequest> collection = parameterLoader.getAllKeyParam();
        if (collection.isEmpty())
            return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.DATA_NOT_FOUND, null, ""), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, collection, ""), HttpStatus.OK);
    }

    private void loadCache(KeyParameter keyParameter) {
        ConcurrentHashMap<String, KeyParameterRequest> hKeyParameter = new ConcurrentHashMap<>();
        hKeyParameter.put(keyParameter.getKey(), keyParameter.toKeyParameterResponse());
        parameterLoader.clearAndPut(RestConstants.CACHE_NAME.KEY_PARAMETER.getValue(), keyParameter.getKey(), hKeyParameter);
    }
}
