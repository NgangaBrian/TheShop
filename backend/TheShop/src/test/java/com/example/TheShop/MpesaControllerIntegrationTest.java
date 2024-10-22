package com.example.TheShop;


import com.example.TheShop.darajaApi.controllers.MpesaController;
import com.example.TheShop.darajaApi.dtos.*;
import com.example.TheShop.database.models.Payments;
import com.example.TheShop.database.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MpesaControllerIntegrationTest {

    @Autowired
    private MpesaController mpesaController;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAcknowledgeSTKResponse_InsertIntoDatabase() throws Exception {
        // Create the mock STKPushAsynchronousResponse
        STKPushAsynchronousResponse stkPushAsynchronousResponse = new STKPushAsynchronousResponse();
        Body body = new Body();
        StkCallback stkCallback = new StkCallback();
        stkCallback.setMerchantRequestID("29115-34620561-1");
        stkCallback.setCheckoutRequestID("ws_CO_191220191020363925");
        stkCallback.setResultCode(0); // Success
        stkCallback.setResultDesc("The service request is processed successfully.");

        CallbackMetadata callbackMetadata = new CallbackMetadata();
        callbackMetadata.setItem(List.of(
                new ItemItem("PhoneNumber", "254708374149"),
                new ItemItem("Amount", "100.00"),
                new ItemItem("TransactionDate", "20231014120000"),
                new ItemItem("TransactionID", "NLJ7RT61SV"),
                new ItemItem("FirstName", "John"),
                new ItemItem("MiddleName", "Doe"),
                new ItemItem("LastName", "Smith")
        ));
        stkCallback.setCallbackMetadata(callbackMetadata);
        body.setStkCallback(stkCallback);
        stkPushAsynchronousResponse.setBody(body);

        // Call the controller method
        ResponseEntity<AcknowledgeResponse> response = mpesaController.acknowledgeSTKResponse(stkPushAsynchronousResponse);

        // Check if data is saved in the database
        List<Payments> paymentsList = (List<Payments>) paymentRepository.findAll();
        assertFalse(paymentsList.isEmpty());

        Payments savedPayment = paymentsList.get(0);
        assertEquals("29115-34620561-1", savedPayment.getMerchantRequestID());
        assertEquals("ws_CO_191220191020363925", savedPayment.getCheckoutRequestID());
        assertEquals(0, savedPayment.getResultCode());
        assertEquals("The service request is processed successfully.", savedPayment.getResultDesc());
        assertEquals("254708374149", savedPayment.getPhoneNumber());
        assertEquals(new BigDecimal("100.00"), savedPayment.getAmount());
        assertEquals("NLJ7RT61SV", savedPayment.getTransactionId());
        assertEquals(LocalDateTime.of(2023, 10, 14, 12, 0), savedPayment.getTransactionTime());
        assertEquals("John", savedPayment.getFirstName());
        assertEquals("Doe", savedPayment.getMiddleName());
        assertEquals("Smith", savedPayment.getLastName());

        // Verify the response
        assertEquals(200, response.getStatusCodeValue());
    }
}


