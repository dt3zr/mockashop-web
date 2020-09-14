package io.dt3zr.mockashopweb;

import io.dt3zr.mockashopweb.api.ApiErrorResponseHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MockashopWebApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.errorHandler(new ApiErrorResponseHandler()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(MockashopWebApplication.class, args);
	}

}
