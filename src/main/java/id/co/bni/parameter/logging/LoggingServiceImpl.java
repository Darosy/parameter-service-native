package id.co.bni.parameter.logging;

import id.co.bni.parameter.util.RestConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Component
public class LoggingServiceImpl implements LoggingService {
    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        MDC.clear();
        Map<String, String> headers = buildHeadersMap(httpServletRequest);
        if(generateIdHeader(httpServletRequest, RestConstants.HEADER_NAME.REQUEST_ID.getValue()))
            headers.put(RestConstants.HEADER_NAME.REQUEST_ID.getValue(), MDC.get(RestConstants.HEADER_NAME.REQUEST_ID.getValue()));
        if(generateIdHeader(httpServletRequest,RestConstants.HEADER_NAME.CORRELATION_ID.getValue()))
            headers.put(RestConstants.HEADER_NAME.CORRELATION_ID.getValue(), MDC.get(RestConstants.HEADER_NAME.CORRELATION_ID.getValue()));

        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("body=[").append(body).append("]");
        }

        log.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, String> headers = buildHeadersMap(httpServletResponse);
        if(!StringUtils.hasLength(httpServletResponse.getHeader(RestConstants.HEADER_NAME.REQUEST_ID.getValue())))
            headers.put(RestConstants.HEADER_NAME.REQUEST_ID.getValue(), MDC.get(RestConstants.HEADER_NAME.REQUEST_ID.getValue()));
        if(!StringUtils.hasLength(httpServletResponse.getHeader(RestConstants.HEADER_NAME.CORRELATION_ID.getValue())))
            headers.put(RestConstants.HEADER_NAME.CORRELATION_ID.getValue(), MDC.get(RestConstants.HEADER_NAME.CORRELATION_ID.getValue()));

        stringBuilder.append("RESPONSE ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
        stringBuilder.append("status=[").append(httpServletResponse.getStatus()).append("] ");
        stringBuilder.append("responseBody=[").append(body).append("] ");

        log.info(stringBuilder.toString());
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private boolean generateIdHeader(HttpServletRequest request, String key) {
        boolean result = false;
        String id = request.getHeader(key);
        if (!StringUtils.hasLength(id)) {
            id = UUID.randomUUID().toString();
            result = true;
        }
        MDC.put(key, id);
        return result;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
