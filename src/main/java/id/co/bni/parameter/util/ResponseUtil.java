package id.co.bni.parameter.util;

import id.co.bni.parameter.dto.ResponseService;

public class ResponseUtil {
    private ResponseUtil() {
    }

    public static ResponseService setResponse(RestConstants.RESPONSE response, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatus(response.getDescription());
        res.setStatusCode(response.getCode());
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseSuccessCustom(RestConstants.RESPONSE response, Object obj, String status, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(response.getCode());
        res.setStatus(status);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

    public static ResponseService setResponseError(String code, String desc, Object obj, String message) {
        ResponseService res = new ResponseService();
        res.setStatusCode(code);
        res.setStatus(desc);
        res.setData(obj);
        res.setMessage(message);
        return res;
    }

}
