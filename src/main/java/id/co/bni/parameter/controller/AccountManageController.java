package id.co.bni.parameter.controller;

import id.co.bni.parameter.dto.ResponseService;
import id.co.bni.parameter.dto.request.AccountManagementRequest;
import id.co.bni.parameter.services.AccountManageService;
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
@RequestMapping(value = "/api/parameter/account")
public class AccountManageController {

    private final AccountManageService accountManageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> create(@RequestBody @Valid AccountManagementRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return accountManageService.create(req);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> update(@RequestBody @Valid AccountManagementRequest req, BindingResult result) {
        ResponseService response = new ResponseService();
        if (!RestValidationHelper.fieldValidation(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return accountManageService.update(req);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> delete(@RequestParam("companyId") @NotBlank @NotNull String companyId) {
        return accountManageService.delete(companyId);
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findByCompanyId(@RequestParam("companyId") @NotBlank @NotNull String companyId) {
        return accountManageService.findByCompanyId(companyId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseService> findAll() {
        return accountManageService.findAll();
    }
}
