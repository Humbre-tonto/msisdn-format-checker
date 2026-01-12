package com.example.msisdnformatchecker.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MsisdnValidationControllerTest {

    @InjectMocks
    private MsisdnValidationController msisdnValidationController;

    @Test
    public void testValidateMsisdn_validMsisdn() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("17063582111");
        request.setCountryCode("US");

        MsisdnValidationController.ValidationResponse response = msisdnValidationController.validateMsisdn(request);

        assertNotNull(response);
        assertEquals(true, response.isValid());
        assertEquals("Valid MSISDN format for country US", response.getMessage());
    }

    @Test
    public void testValidateMsisdn_invalidMsisdn() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("12345");
        request.setCountryCode("US");

        MsisdnValidationController.ValidationResponse response = msisdnValidationController.validateMsisdn(request);

        assertNotNull(response);
        assertEquals(false, response.isValid());
        assertEquals("Number is not a valid mobile number internationally.", response.getMessage());
    }

    @Test
    public void testValidateMsisdn_invalidCountryCode() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("17063582111");
        request.setCountryCode("ZZ");

        MsisdnValidationController.ValidationResponse response = msisdnValidationController.validateMsisdn(request);

        assertNotNull(response);
        assertEquals(false, response.isValid());
        assertEquals("Invalid MSISDN format: Missing or invalid default region.", response.getMessage());
    }

    @Test
    public void testValidateMsisdn_internationalNumber() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("+442079460837");
        request.setCountryCode("GB");

        MsisdnValidationController.ValidationResponse response = msisdnValidationController.validateMsisdn(request);

        assertNotNull(response);
        assertEquals(true, response.isValid());
    }
}
