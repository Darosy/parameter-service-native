package id.co.bni.parameter.controller;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.McpParameterRequest;
import id.co.bni.parameter.services.McpParameterService;
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
@RequestMapping(value = "/api/parameter/mcp")
public class McpParameterController {
    
    private final McpParameterService mcpParameterService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> create(@RequestBody @Valid McpParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return mcpParameterService.create(req);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> update(@RequestBody @Valid McpParameterRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return mcpParameterService.update(req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> delete(@RequestParam("mcpId") @NotBlank @NotNull String mcpId) {
        return mcpParameterService.delete(mcpId);
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findByTransCode(@RequestParam("mcpId") @NotBlank @NotNull String mcpId) {
        return mcpParameterService.findByMcpId(mcpId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findAll() {
        return mcpParameterService.findAll();
    }
}
