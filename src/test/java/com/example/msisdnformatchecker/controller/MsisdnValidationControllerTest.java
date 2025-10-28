package com.example.msisdnformatchecker.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MsisdnValidationControllerTest {

    private final MsisdnValidationController controller = new MsisdnValidationController();

    @Test
    void testValidMsisdn() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("17063428839");
        request.setCountryCode("US");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertTrue(response.isValid());
        assertEquals("Valid MSISDN format for country US", response.getMessage());
    }

    @Test
    void testInvalidMsisdn() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("12345");
        request.setCountryCode("US");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertFalse(response.isValid());
    }

    @Test
    void testValidMsisdnWithPlus() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("+17063428839");
        request.setCountryCode("US");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertTrue(response.isValid());
        assertEquals("Valid MSISDN format for country US", response.getMessage());
    }

    @Test
    void testInvalidCountryCode() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("17063428839");
        request.setCountryCode("ZZ");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertFalse(response.isValid());
    }

    @Test
    void testInternationalNumber() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("442071234567");
        request.setCountryCode("GB");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertTrue(response.isValid());
    }

    @Test
    void testInvalidInternationalNumber() {
        MsisdnValidationController.MsisdnRequest request = new MsisdnValidationController.MsisdnRequest();
        request.setMsisdn("99999999999999999999");
        request.setCountryCode("ZZ");
        MsisdnValidationController.ValidationResponse response = controller.validateMsisdn(request);
        assertFalse(response.isValid());
    }
}
