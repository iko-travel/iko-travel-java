package travel.iko.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Created on: 8/8/21.
 *
 * @author Bjorn Harvold
 * Responsibility:
 */
@Configuration
public class OAuth2WebClientConfiguration {
    @Value("${iko.travel.token-uri}")
    private String tokenUri;
    
    @Value("${iko.travel.client.id}")
    private String clientId;
    
    @Value("${iko.travel.client.secret}")
    private String clientSecret;
    
    @Value("${iko.travel.scopes}") 
    private String scope;

    @Bean(name = "oauth2WebClient")
    WebClient webClient() {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        reactiveClientRegistrationRepository(),
                        reactiveOAuth2AuthorizedClientService()
                        )
        );
        oauth.setDefaultClientRegistrationId("iko-travel");
        return WebClient.builder()
                .filter(oauth)
                .build();
    }

    @Bean
    ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService() {
        return new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository());
    }

    @Bean
    ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository();
    }
    
    @Bean
    ClientRegistration clientRegistration() {
        return ClientRegistration
                .withRegistrationId("iko-travel")
                .tokenUri(tokenUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(scope)
                .build();
    }
}
