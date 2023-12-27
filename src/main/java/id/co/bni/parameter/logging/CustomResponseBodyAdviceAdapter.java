package id.co.bni.parameter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final LoggingService loggingService;

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType, @NonNull Class selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        if (request instanceof ServletServerHttpRequest req && response instanceof ServletServerHttpResponse res) {
            String str = "";
            try {
                str = new ObjectMapper().writeValueAsString(body);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            final String bodyStr = str;
            Optional.ofNullable(body).ifPresentOrElse(value ->
                            loggingService.logResponse(req.getServletRequest(), res.getServletResponse(), bodyStr),
                    () -> loggingService.logResponse(req.getServletRequest(), res.getServletResponse(), body));
        }
        return body;
    }
}
