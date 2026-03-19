package energy.eddie.regionconnector.de.eta.config;

import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.InvalidApiBaseUrlException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.InvalidTimeoutConfigurationException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.MissingCredentialsException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.SslConfigurationException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Consumer;

@Configuration
public class EtaPlusClientConfig {

    @Bean(name = "etaWebClient")
    public WebClient etaWebClient(
            WebClient.Builder webClientBuilder,
            DeEtaPlusConfiguration configuration,
            @Value("${region.connector.de.eta.ssl.enabled:true}") boolean isSslEnabled
    ) throws SSLException {

        validateConfiguration(configuration, isSslEnabled);

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(configuration.responseTimeoutSeconds()));

        if (isSslEnabled) {
            SslContext sslContext = SslContextBuilder.forClient().build();
            httpClient = httpClient.secure(sslSpec -> sslSpec.sslContext(sslContext));
        }

        Consumer<HttpHeaders> authHeaderConsumer = (HttpHeaders headers) -> {
            headers.setBasicAuth(
                    configuration.apiClientId(),
                    configuration.apiClientSecret(),
                    StandardCharsets.UTF_8
            );
        };

        return webClientBuilder
                .baseUrl(configuration.apiBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(authHeaderConsumer)
                .build();
    }

    private void validateConfiguration(DeEtaPlusConfiguration configuration, boolean isSslEnabled) {
        if (configuration.apiBaseUrl() == null || configuration.apiBaseUrl().isBlank()) {
            throw new InvalidApiBaseUrlException("API base URL must not be null or empty.");
        }

        if (isSslEnabled && !configuration.apiBaseUrl().startsWith("https")) {
            throw new SslConfigurationException(
                    "SSL is enabled but the API base URL uses HTTP: " + configuration.apiBaseUrl()
                            + ". Either use an HTTPS URL or disable SSL."
            );
        }

        if (!isSslEnabled && configuration.apiBaseUrl().startsWith("https")) {
            throw new SslConfigurationException(
                    "SSL is disabled but the API base URL uses HTTPS: " + configuration.apiBaseUrl()
                            + ". Either use an HTTP URL or enable SSL."
            );
        }

        if (configuration.apiClientId() == null || configuration.apiClientId().isBlank()) {
            throw new MissingCredentialsException("API client ID must not be null or empty.");
        }

        if (configuration.apiClientSecret() == null || configuration.apiClientSecret().isBlank()) {
            throw new MissingCredentialsException("API client secret must not be null or empty.");
        }

        if (configuration.responseTimeoutSeconds() <= 0) {
            throw new InvalidTimeoutConfigurationException(
                    "Response timeout must be positive, but was: " + configuration.responseTimeoutSeconds() + " seconds."
            );
        }
    }
}