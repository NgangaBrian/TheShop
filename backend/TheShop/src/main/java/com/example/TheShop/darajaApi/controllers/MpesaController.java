package com.example.TheShop.darajaApi.controllers;

import com.example.TheShop.darajaApi.dtos.*;
import com.example.TheShop.darajaApi.services.DarajaApi;
import com.example.TheShop.database.models.Payments;
import com.example.TheShop.database.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.TheShop.darajaApi.utils.HelperUtility.logResponseToFile;

@Slf4j
@RestController
@RequestMapping("/mobile-money")
public class MpesaController {

    private final DarajaApi darajaApi;
    private final AcknowledgeResponse acknowledgeResponse;
    public final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    public MpesaController(DarajaApi darajaApi, AcknowledgeResponse acknowledgeResponse, ObjectMapper objectMapper, PaymentRepository paymentRepository) {
        this.darajaApi = darajaApi;
        this.acknowledgeResponse = acknowledgeResponse;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping(value = "/token", produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        return ResponseEntity.ok(darajaApi.getAccessToken());
    }

    // todo :- /register-url (Registering Preferred Validation & confirmation URL...)
    /*
        {
            "ShortCode": "601426",
            "ResponseType":"[Cancelled/Completed]",
            "ConfirmationURL":"[confirmation URL]",
            "ValidationURL":"[validation URL]"
        }

        {
            "OriginatorCoversationID": "7619-37765134-1",
            "ResponseCode": "0",
            "ResponseDescription": "success"
        }
        {
            "OriginatorCoversationID": "8c39-4e24-ba78-a464bfcb7db629839",
            "ResponseCode": "0",
            "ResponseDescription": "Success"
        }

     */
    @PostMapping(value = "/register-url", produces = "application/json")
    public ResponseEntity<RegisterUrlResponse> registerUrl() {
        return ResponseEntity.ok(darajaApi.registerUrl());
    }
    // todo :- /validation (Transaction Validation Endpoint...)

    @PostMapping(value = "/validation", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> validateTransaction(@RequestBody TransactionResult transactionResul){
        return ResponseEntity.ok(acknowledgeResponse);
    }

    // todo :- /simulate-c2b (Endpoint to simulate C2B Transactions...)

    // todo :- Mpesa STK Push

    /*
    Request
    {
       "BusinessShortCode": "174379",
       "Password": "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3",
       "Timestamp":"20160216165627",
       "TransactionType": "CustomerPayBillOnline",
       "Amount": "1",
       "PartyA":"254708374149",
       "PartyB":"174379",
       "PhoneNumber":"254708374149",
       "CallBackURL": "https://mydomain.com/pat",
       "AccountReference":"Test",
       "TransactionDesc":"Test"
    }

    Stk password = base64 encoding of (shortcode + passkey + timestamp)

Internal Request

    {
        "PhoneNumber":"254790944432",
        "Amount" : "10"
    }
    Synchronous Response
    {
       "MerchantRequestID": "29115-34620561-1",
       "CheckoutRequestID": "ws_CO_191220191020363925",
       "ResponseCode": "0",
       "ResponseDescription": "Success. Request accepted for processing",
       "CustomerMessage": "Success. Request accepted for processing"
    }
    Async Response

    {
   "Body": {
      "stkCallback": {
         "MerchantRequestID": "29115-34620561-1",
         "CheckoutRequestID": "ws_CO_191220191020363925",
         "ResultCode": 0,
         "ResultDesc": "The service request is processed successfully.",
         "CallbackMetadata": {
            "Item": [{
               "Name": "Amount",
               "Value": 1.00
            },
            {
               "Name": "MpesaReceiptNumber",
               "Value": "NLJ7RT61SV"
            },
            {
               "Name": "TransactionDate",
               "Value": 20191219102115
            },
            {
               "Name": "PhoneNumber",
               "Value": 254708374149
            }]
         }
      }
   }
}
     */

    @PostMapping(path = "/stk-transaction-request", produces = "application/json")
    public ResponseEntity<STKPushSynchronousResponse> performSTKPushTransaction(@RequestBody InternalSTKPushRequest internalSTKPushRequest){
        return ResponseEntity.ok(darajaApi.performSTKPushRequest(internalSTKPushRequest));
    }

    @SneakyThrows
    @PostMapping(path = "/stk-transaction-result", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> acknowledgeSTKResponse (@RequestBody STKPushAsynchronousResponse stkPushAsynchronousResponse){
        log.info("==========STK Push Async Response============");
        String rawCallbackResponse = objectMapper.writeValueAsString(stkPushAsynchronousResponse);
        log.info("Mpesa raw callback response"+rawCallbackResponse);

        logResponseToFile(rawCallbackResponse);

        Long userId = null;
        String merchantRequestID = stkPushAsynchronousResponse.getBody().getStkCallback().getMerchantRequestID();
        String checkoutRequestID = stkPushAsynchronousResponse.getBody().getStkCallback().getCheckoutRequestID();
        int resultCode = stkPushAsynchronousResponse.getBody().getStkCallback().getResultCode();
        String resultDesc = stkPushAsynchronousResponse.getBody().getStkCallback().getResultDesc();

        // Initialize variables to store details
        String phoneNumer = null;
        BigDecimal amount = null;
        LocalDateTime transTime = null;
        String firstName = null;
        String middleName = null;
        String lastName = null;
        String transId = null;

        CallbackMetadata callbackMetadata = stkPushAsynchronousResponse.getBody().getStkCallback().getCallbackMetadata();
        for (ItemItem item : callbackMetadata.getItem()){
            switch (item.getName()){
                case "PhoneNumber":
                    phoneNumer=item.getValue();
                    break;
                case "Amount":
                    amount = new BigDecimal(item.getValue());
                    break;
                case "TransactionTime":
                    transTime = LocalDateTime.parse(item.getValue(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    break;
                case "TransactionID":
                    transId = item.getValue();
                    break;
                case "FirstName":
                    firstName = item.getValue();
                    break;
                case "MiddleName":
                    middleName = item.getValue();
                    break;
                case "LastName":
                    lastName = item.getValue();
                    break;
                default:
                    break;
            }
        }

        if(resultCode == 0){
            log.info("Transaction Successful");
            // Further processing like updating the database or notifying the user that transaction is successful
            Payments payments = new Payments();
            payments.setUserId(userId);
            payments.setAmount(amount);
            payments.setMerchantRequestID(merchantRequestID);
            payments.setCheckoutRequestID(checkoutRequestID);
            payments.setFirstName(firstName);
            payments.setMiddleName(middleName);
            payments.setLastName(lastName);
            payments.setResultCode(resultCode);
            payments.setResultDesc(resultDesc);
            payments.setPhoneNumber(phoneNumer);
            payments.setTransactionId(transId);
            payments.setTransactionTime(transTime);

            paymentRepository.save(payments);
        } else {
            log.info("Transaction Failed");
            // Further processing, like telling the user transaction failed
        }
        return ResponseEntity.ok(acknowledgeResponse);
    }

}
