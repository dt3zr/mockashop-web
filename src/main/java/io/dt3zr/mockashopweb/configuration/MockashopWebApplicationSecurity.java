package io.dt3zr.mockashopweb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@EnableWebSecurity
public class MockashopWebApplicationSecurity {

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
        @Value("${oauth.facebook.clientId}")
        private String clientId;

        @Value("${oauth.facebook.clientSecret}")
        private String clientSecret;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.oauth2Login(Customizer.withDefaults()).antMatcher("/**").authorizeRequests().anyRequest().authenticated().and().logout().logoutSuccessUrl("/home");

//                    .and().logout().logoutSuccessUrl("/");
            // override default
//                    .oauth2Login().clientRegistrationRepository(googleClientRegistrationRepository());
        }

        private ClientRegistrationRepository googleClientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(googleClientRegistration());
        }

        private ClientRegistration googleClientRegistration() {
            return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                    .clientId("1186385488409635")
                    .clientSecret("b4309803343226029a254d73a8fd365e")
                    .build();
        }

        private ClientRegistrationRepository clientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(facebookClientRegistration());
        }

        private ClientRegistration facebookClientRegistration() {
            return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(facebookClientRegistration());
    }

    private ClientRegistration facebookClientRegistration() {
        return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
                .clientId("1186385488409635")
                .clientSecret("b4309803343226029a254d73a8fd365e")
                .build();
    }
}


