package com.example.msisdnformatchecker.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MsisdnValidationController.class);

    @Operation(summary = "Validate MSISDN number", description = "Checks if the provided MSISDN number is valid for the specified country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation result", 
            content = @Content(schema = @Schema(implementation = ValidationResponse.class)))
    })
    @PostMapping("/validate")
    public ValidationResponse validateMsisdn(@RequestBody MsisdnRequest request) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
         //log the incoming requestbody
        logger.info("Received MSISDN validation request: {} for country: {}", request.getMsisdn(), request.getCountryCode());
        try {
            String msisdn = request.getMsisdn();
            //if the msisdn contains + value remove it from the msisdn
            if (msisdn.startsWith("+")) {
                msisdn = msisdn.substring(1);
                request.setMsisdn(msisdn);
                logger.info("MSISDN after removing +: {}", msisdn);
            }
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(request.getMsisdn(), request.getCountryCode());
            boolean isValid = phoneUtil.isValidNumberForRegion(phoneNumber, request.getCountryCode());
            logger.info("isValid msisdn : {} for country: {}", isValid, request.getCountryCode());
            if (isValid) {
                ValidationResponse response = new ValidationResponse(true,
                    "Valid MSISDN format for country " + request.getCountryCode());
                logger.info("Validation result: {} - {}", response.isValid(), response.getMessage());
                return response;
            } else {
                // MSISDN is not valid for given country code, so format it as international number
                try {
                    ValidationResponse international = validateInternational(phoneUtil, msisdn);
                    if (international.isValid()) {
                        return international;
                    }
                    // ponytail: retry once without a leading 234/+234 gateway prefix
                    String stripped = msisdn.startsWith("+234") ? msisdn.substring(4)
                            : msisdn.startsWith("234") ? msisdn.substring(3) : null;
                    if (stripped != null) {
                        logger.info("Retrying international validation without 234 prefix: {}", stripped);
                        return validateInternational(phoneUtil, stripped);
                    }
                    return international;
                } catch (NumberParseException ex) {
                    return new ValidationResponse(false, "Could not parse as international number: " + ex.getMessage());
                }
            }
        } catch (NumberParseException e) {
            return new ValidationResponse(false, "Invalid MSISDN format: " + e.getMessage());
        }
    }

    private ValidationResponse validateInternational(PhoneNumberUtil phoneUtil, String msisdn) throws NumberParseException {
        Phonenumber.PhoneNumber parsedMsisdn = phoneUtil.parse(msisdn.startsWith("+") ? msisdn : "+" + msisdn, null);
        boolean isInternationalValid = phoneUtil.isValidNumber(parsedMsisdn);
        PhoneNumberUtil.PhoneNumberType numberType = phoneUtil.getNumberType(parsedMsisdn);
        logger.info("Validation International number result: {} - with numberType: {}", isInternationalValid, numberType);

        if (isInternationalValid && isAvailableNumberType(numberType)) {
            return new ValidationResponse(true, "Formatted as international mobile: " + parsedMsisdn);
        }
        return new ValidationResponse(false, "Number is not a valid mobile number internationally.");
    }

    private boolean isAvailableNumberType(PhoneNumberUtil.PhoneNumberType numberType) {
        return numberType == PhoneNumberUtil.PhoneNumberType.MOBILE ||
               numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE;
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
