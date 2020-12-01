package com.example.oauthtutor.config;

import com.example.oauthtutor.common.security.CustomOAuth2Provider;
import com.example.oauthtutor.common.security.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private ClientRegistrationConfig clientRegistrationConfig;

    @ConfigurationProperties(prefix = "spring.security.oauth2.client")
    @Bean
    public ClientRegistrationConfig clientRegistrationConfig() {
        return new ClientRegistrationConfig();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(a -> a
                        .antMatchers("/", "/login", "/login/**", "/error", "/webjars/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .csrf().disable()
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                )
                .oauth2Login()
                .authorizationEndpoint()
                    .authorizationRequestRepository(authorizationRequestRepository())
                    .and()
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(auth2AuthorizedClientService());
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    private static List<String> clients = Arrays.asList("google", "facebook", "kakao", "local");

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        this.clientRegistrationConfig = clientRegistrationConfig();

        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        if (clientRegistrationConfig == null ||
                clientRegistrationConfig.getRegistration().get(client) == null)
            return null;

        ClientRegistrationConfig.Registration registration = clientRegistrationConfig.getRegistration().get(client);
        switch (client) {
            case "google":
                return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                        .clientId(registration.getClientId())
                        .clientSecret(registration.getClientSecret()).build();
            case "facebook":
                return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                        .clientId(registration.getClientId())
                        .clientSecret(registration.getClientSecret()).build();
            case "kakao":
                return CustomOAuth2Provider.KAKAO.getBuilder(client)
                        .clientId(registration.getClientId())
                        .clientSecret(registration.getClientSecret())
                        .jwkSetUri("temp").build();
            case "local":
                return CustomOAuth2Provider.LOCAL.getBuilder(client)
                        .clientId(registration.getClientId())
                        .clientSecret(registration.getClientSecret())
                        .scope("read")
                        .build();
        }
        return null;
    }
}
