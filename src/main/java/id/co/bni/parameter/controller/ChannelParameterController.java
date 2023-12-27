package id.co.bni.parameter.controller;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.ChannelParameterRequest;
import id.co.bni.parameter.services.ChannelParameterService;
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
@RequestMapping(value = "/api/parameter/channel")
public class ChannelParameterController {

    private final ChannelParameterService channelParameterService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> create(@RequestBody @Valid ChannelParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return channelParameterService.create(req);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> update(@RequestBody @Valid ChannelParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return channelParameterService.update(req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> delete(@RequestParam("channelId") @NotBlank @NotNull String channelId, @RequestParam("systemId") @NotBlank @NotNull String systemId) {
        return channelParameterService.delete(channelId, systemId);
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findByChannelIdAndSystemId(@RequestParam("channelId") @NotBlank @NotNull String channelId, @RequestParam("systemId") @NotBlank @NotNull String systemId) {
        return channelParameterService.findByChannelIdAndSystemId(channelId, systemId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findAll() {
        return channelParameterService.findAll();
    }
}
