package com.example.TheShop;

import com.example.TheShop.darajaApi.dtos.AcknowledgeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TheShopApplication {

	public static void main(String[] args) {
		System.out.println("Hello World");
		SpringApplication.run(TheShopApplication.class, args);
	}

	@Bean
	public OkHttpClient getOkHttpClient(){
		return new OkHttpClient();
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}

	@Bean
	public AcknowledgeResponse getAcknowledgeResponse(){
		AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse();
		acknowledgeResponse.setMessage("Success");
		return acknowledgeResponse;
	}
}
