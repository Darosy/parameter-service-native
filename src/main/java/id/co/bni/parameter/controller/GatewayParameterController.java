package id.co.bni.parameter.controller;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.GatewayParameterRequest;
import id.co.bni.parameter.services.GatewayParameterService;
import id.co.bni.parameter.util.RestValidationHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/parameter/gateway")
public class GatewayParameterController {

    private final GatewayParameterService gatewayParameterService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> create(@RequestBody @Valid GatewayParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return gatewayParameterService.create(req);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> update(@RequestBody @Valid GatewayParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return gatewayParameterService.update(req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> delete(@RequestParam("transCode") @NotBlank @NotNull String transCode, @RequestParam("systemIdOrmcpId") @NotBlank @NotNull String systemIdOrmcpId, @RequestParam("paymentType") @NotNull String paymentType) {
        return gatewayParameterService.delete(transCode, systemIdOrmcpId, paymentType);
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findByTransCode(@RequestParam("transCode") @NotBlank @NotNull String transCode, @RequestParam("systemIdOrmcpId") @NotBlank @NotNull String systemIdOrmcpId, @RequestParam("paymentType") @NotNull String paymentType) {
        return gatewayParameterService.findByTransCodeAndSystemIdOrmcpIdAndPaymentType(transCode, systemIdOrmcpId, paymentType);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findAll() {
        return gatewayParameterService.findAll();
    }
}
