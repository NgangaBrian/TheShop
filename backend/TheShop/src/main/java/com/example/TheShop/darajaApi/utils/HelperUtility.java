package com.example.TheShop.darajaApi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class HelperUtility {
    /**
     * @param value the value to be converted to base64 string
     * @return returns base54 string
     */

    public static String toBase64String(String value){
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String toJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getTransactionUniqueNumber(){
        RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().
                withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        String transactionNumber = stringGenerator.generate(12).toUpperCase();

        log.info("Transaction Number " + transactionNumber);
        return transactionNumber;
    }

    public static String getSTKPushPassword(String shortcode, String passKey, String timestamp){
        String  concatenatedString = shortcode + passKey + timestamp;
        return toBase64String(concatenatedString);
    }

    public static String getTransactionTimestamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static void logResponseToFile(String response) throws IOException {
        FileWriter fileWriter = new FileWriter("MpesaStkResponse.json", true);
        fileWriter.write(response);
        fileWriter.write("\n");
        fileWriter.close();
    }
}
