package id.co.bni.parameter.controller;

import id.co.bni.parameter.services.ReadinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/db")
public class ReadinessController {
    private final ReadinessService service;

    @GetMapping(value = "/health/readiness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> findByChannelIdAndSystemId() {
        boolean valid = service.isDatabaseConnected();
        String status = "DOWN";
        if (valid) {
            status = "UP";
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        return new ResponseEntity<>(response, valid ? HttpStatus.OK: HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
