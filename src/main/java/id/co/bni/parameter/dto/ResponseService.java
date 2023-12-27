package id.co.bni.parameter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseService {
    private Object status;
    private Object statusCode;
    private Object message;
    private Object data;
}
