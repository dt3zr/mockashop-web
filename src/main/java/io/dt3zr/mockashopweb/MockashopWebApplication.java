package io.dt3zr.mockashopweb;

import io.dt3zr.mockashopweb.api.ApiErrorResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MockashopWebApplication {

	private static final Logger log = LoggerFactory.getLogger(MockashopWebApplication.class);

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, OAuth2AuthorizedClientService authorizedClientService) {
		log.info("{}", authorizedClientService.loadAuthorizedClient("mockashop-api", "anonymousUser"));
		return restTemplateBuilder.errorHandler(new ApiErrorResponseHandler()).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(MockashopWebApplication.class, args);
	}

}
