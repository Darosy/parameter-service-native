package id.co.bni.parameter.util;

import id.co.bni.parameter.dto.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Slf4j
public class RestValidationHelper {
    private RestValidationHelper() {

    }
    public static boolean fieldValidation(BindingResult result, ResponseService response){
        if (result != null && result.hasErrors()) {
            response.setStatusCode(RestConstants.RESPONSE.WRONG_FORMAT_DATA.getCode());
            response.setStatus(RestConstants.RESPONSE.WRONG_FORMAT_DATA.getDescription());
            response.setMessage(Objects.requireNonNull(result.getFieldError()).getField() + " - " + Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            log.error(Objects.requireNonNull(result.getFieldError()).getField() + " - " + Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return false;
        }
        return true;
    }
}
