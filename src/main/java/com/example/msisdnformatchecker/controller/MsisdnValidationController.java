package com.example.msisdnformatchecker.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/msisdn")
@Tag(name = "MSISDN Validation", description = "API for validating MSISDN numbers")
public class MsisdnValidationController {

    @Operation(summary = "Validate MSISDN number", description = "Checks if the provided MSISDN number is valid for the specified country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation result", 
            content = @Content(schema = @Schema(implementation = ValidationResponse.class)))
    })
    @PostMapping("/validate")
    public ValidationResponse validateMsisdn(@RequestBody MsisdnRequest request) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(request.getMsisdn(), request.getCountryCode());
            boolean isValid = phoneUtil.isValidNumberForRegion(phoneNumber, request.getCountryCode());
            return new ValidationResponse(isValid, 
                isValid ? "Valid MSISDN format for country " + request.getCountryCode() 
                        : "Invalid MSISDN format for country " + request.getCountryCode());
        } catch (NumberParseException e) {
            return new ValidationResponse(false, "Invalid MSISDN format: " + e.getMessage());
        }
    }

    static class MsisdnRequest {
        private String msisdn;
        private String countryCode;

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }

    static class ValidationResponse {
        private boolean valid;
        private String message;

        public ValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}