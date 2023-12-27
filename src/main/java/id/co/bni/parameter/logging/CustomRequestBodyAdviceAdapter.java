package id.co.bni.parameter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Optional;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final LoggingService loggingService;

    private final HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        String str = "";
        try {
            str = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        final String bodyStr = str;

        Optional.of(body).ifPresentOrElse(value ->
                        loggingService.logRequest(httpServletRequest, bodyStr),
                () -> loggingService.logRequest(httpServletRequest, body));

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

}
