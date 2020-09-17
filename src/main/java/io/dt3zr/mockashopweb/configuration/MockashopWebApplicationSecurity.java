package io.dt3zr.mockashopweb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@EnableWebSecurity
public class MockashopWebApplicationSecurity {
    @Value("${oauth.facebook.clientId}")
    private String clientId;

    @Value("${oauth.facebook.clientSecret}")
    private String clientSecret;

    @Order(1)
    @Configuration
    public static class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").authorizeRequests().anyRequest().permitAll();
        }
    }

    @Order(2)
    @Configuration
    public static class HomeSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/home").authorizeRequests().anyRequest().permitAll();
        }
    }

    @Order(3)
    @Configuration
    public static class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                    .authorizeRequests().anyRequest().authenticated()
                    .and().oauth2Login();

//            http.oauth2Login(Customizer.withDefaults()).antMatcher("/**").authorizeRequests().anyRequest().authenticated().and().logout().logoutSuccessUrl("/home");
//                    .and().logout().logoutSuccessUrl("/");
            // override default
//                    .oauth2Login().clientRegistrationRepository(googleClientRegistrationRepository());
        }
//
//        private ClientRegistrationRepository clientRegistrationRepository() {
//            return new InMemoryClientRegistrationRepository(facebookClientRegistration());
//        }

//        private ClientRegistration facebookClientRegistration() {
//            return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
//                    .clientId(clientId)
//                    .clientSecret(clientSecret)
//                    .build();
//        }
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration demoClientRegistration = ClientRegistrations
                .fromIssuerLocation("http://localhost:7070/auth/realms/Demo")
                .registrationId("mockashop-api")
                .clientId("mockashop-api-client")
                .clientSecret("6ff6f8c7-d25c-457a-ae7c-e5cf369f7afa")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("http://localhost:7070/auth/realms/Demo/protocol/openid-connect/token")
                .build();

        ClientRegistration facebookClientRegistration =
                CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        return new InMemoryClientRegistrationRepository(demoClientRegistration, facebookClientRegistration);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider
                = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager
                = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

}


