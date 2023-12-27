package id.co.bni.parameter.controller;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.KeyParameterRequest;
import id.co.bni.parameter.services.KeyParameterService;
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
@RequestMapping(value = "/api/parameter/key")
public class KeyParameterController {
    private final KeyParameterService keyParameterService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> create(@RequestBody @Valid KeyParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return keyParameterService.create(req);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> update(@RequestBody @Valid KeyParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return keyParameterService.update(req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> delete(@RequestParam("key") @NotBlank @NotNull String key) {
        return keyParameterService.delete(key);
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findByChannelIdAndSystemId(@RequestParam("key") @NotBlank @NotNull String key) {
        return keyParameterService.findByKey(key);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findAll() {
        return keyParameterService.findAll();
    }
}
