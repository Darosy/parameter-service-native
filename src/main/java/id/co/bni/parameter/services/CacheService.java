package id.co.bni.parameter.services;

import id.co.bni.parameter.cache.ParameterLoader;
import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.util.ResponseUtil;
import id.co.bni.parameter.util.RestConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

//    private final RedisTemplate<String, String> redisTemplate;
    private final ParameterLoader parameterLoader;

    public ResponseService reloadByKey(String name, String key) {
//        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
//        if (Boolean.TRUE.equals(hashOptions.getOperations().hasKey(name))) {
//            hashOptions.delete(name, key);
//            hashOptions.getOperations().de
//        }
        parameterLoader.deleteHash(name, key);
        return ResponseUtil.setResponse(RestConstants.RESPONSE.APPROVED, true, "");
    }

}
