package energy.eddie.regionconnector.de.eta.config;

import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.InvalidApiBaseUrlException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.InvalidTimeoutConfigurationException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.MissingCredentialsException;
import energy.eddie.regionconnector.de.eta.exceptions.EtaPlusClientConfigExceptions.SslConfigurationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EtaPlusClientConfigTest {

    private static MockWebServer server;
    private static final EtaPlusClientConfig CONFIG = new EtaPlusClientConfig();

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

    private static DeEtaPlusConfiguration configFor(String baseUrl) {
        return new DeEtaPlusConfiguration(
                "eligible-party",
                baseUrl,
                "my-client",
                "my-secret",
                "/api/meters/historical",
                "/api/v1/permissions/{id}",
                30
        );
    }

    private static DeEtaPlusConfiguration configWithCredentials(String baseUrl, String clientId, String clientSecret) {
        return new DeEtaPlusConfiguration(
                "eligible-party",
                baseUrl,
                clientId,
                clientSecret,
                "/api/meters/historical",
                "/api/v1/permissions/{id}",
                30
        );
    }

    private static DeEtaPlusConfiguration configWithTimeout(int timeoutSeconds) {
        return new DeEtaPlusConfiguration(
                "eligible-party",
                "http://localhost",
                "my-client",
                "my-secret",
                "/api/meters/historical",
                "/api/v1/permissions/{id}",
                timeoutSeconds
        );
    }

    // --- Happy path tests ---

    @Test
    void etaWebClient_returnsNonNullWebClient() throws SSLException {
        String baseUrl = "http://localhost:" + server.getPort();

        WebClient webClient = CONFIG.etaWebClient(WebClient.builder(), configFor(baseUrl), false);

        assertThat(webClient).isNotNull();
    }

    @Test
    void etaWebClient_withHttpsUrlAndSslEnabled_doesNotThrow() {
        assertThatCode(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configFor("https://api.eta-plus.de"),
                true
        )).doesNotThrowAnyException();
    }

    @Test
    void etaWebClient_setsCorrectBasicAuthHeader() throws SSLException, InterruptedException {
        String baseUrl = "http://localhost:" + server.getPort();
        WebClient webClient = CONFIG.etaWebClient(WebClient.builder(), configFor(baseUrl), false);

        server.enqueue(new MockResponse().setResponseCode(200));
        webClient.get().uri("/test").retrieve().toBodilessEntity()
                .onErrorResume(e -> Mono.empty())
                .block();

        RecordedRequest request = server.takeRequest();
        String authHeader = request.getHeader("Authorization");
        String expectedCredentials = Base64.getEncoder()
                .encodeToString("my-client:my-secret".getBytes(StandardCharsets.UTF_8));
        assertThat(authHeader).isEqualTo("Basic " + expectedCredentials);
    }

    @Test
    void etaWebClient_usesBaseUrlFromConfiguration() throws SSLException, InterruptedException {
        String baseUrl = "http://localhost:" + server.getPort();
        WebClient webClient = CONFIG.etaWebClient(WebClient.builder(), configFor(baseUrl), false);

        server.enqueue(new MockResponse().setResponseCode(200));
        webClient.get().uri("/my-path").retrieve().toBodilessEntity()
                .onErrorResume(e -> Mono.empty())
                .block();

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath()).isEqualTo("/my-path");
    }

    // --- SSL mismatch tests ---

    @Test
    void etaWebClient_withHttpUrlAndSslEnabled_throwsSslConfigurationException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configFor("http://api.eta-plus.de"),
                true
        ))
                .isInstanceOf(SslConfigurationException.class)
                .hasMessageContaining("SSL is enabled but the API base URL uses HTTP");
    }

    @Test
    void etaWebClient_withHttpsUrlAndSslDisabled_throwsSslConfigurationException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configFor("https://api.eta-plus.de"),
                false
        ))
                .isInstanceOf(SslConfigurationException.class)
                .hasMessageContaining("SSL is disabled but the API base URL uses HTTPS");
    }

    // --- Invalid API base URL tests ---

    @Test
    void etaWebClient_withNullBaseUrl_throwsInvalidApiBaseUrlException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials(null, "my-client", "my-secret"),
                false
        ))
                .isInstanceOf(InvalidApiBaseUrlException.class)
                .hasMessageContaining("API base URL must not be null or empty");
    }

    @Test
    void etaWebClient_withEmptyBaseUrl_throwsInvalidApiBaseUrlException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("", "my-client", "my-secret"),
                false
        ))
                .isInstanceOf(InvalidApiBaseUrlException.class)
                .hasMessageContaining("API base URL must not be null or empty");
    }

    @Test
    void etaWebClient_withBlankBaseUrl_throwsInvalidApiBaseUrlException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("   ", "my-client", "my-secret"),
                false
        ))
                .isInstanceOf(InvalidApiBaseUrlException.class)
                .hasMessageContaining("API base URL must not be null or empty");
    }

    // --- Missing credentials tests ---

    @Test
    void etaWebClient_withNullClientId_throwsMissingCredentialsException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("http://localhost", null, "my-secret"),
                false
        ))
                .isInstanceOf(MissingCredentialsException.class)
                .hasMessageContaining("API client ID must not be null or empty");
    }

    @Test
    void etaWebClient_withEmptyClientId_throwsMissingCredentialsException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("http://localhost", "", "my-secret"),
                false
        ))
                .isInstanceOf(MissingCredentialsException.class)
                .hasMessageContaining("API client ID must not be null or empty");
    }

    @Test
    void etaWebClient_withNullClientSecret_throwsMissingCredentialsException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("http://localhost", "my-client", null),
                false
        ))
                .isInstanceOf(MissingCredentialsException.class)
                .hasMessageContaining("API client secret must not be null or empty");
    }

    @Test
    void etaWebClient_withEmptyClientSecret_throwsMissingCredentialsException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithCredentials("http://localhost", "my-client", ""),
                false
        ))
                .isInstanceOf(MissingCredentialsException.class)
                .hasMessageContaining("API client secret must not be null or empty");
    }

    // --- Timeout configuration tests ---

    @Test
    void etaWebClient_withZeroTimeout_throwsInvalidTimeoutConfigurationException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithTimeout(0),
                false
        ))
                .isInstanceOf(InvalidTimeoutConfigurationException.class)
                .hasMessageContaining("Response timeout must be positive");
    }

    @Test
    void etaWebClient_withNegativeTimeout_throwsInvalidTimeoutConfigurationException() {
        assertThatThrownBy(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithTimeout(-5),
                false
        ))
                .isInstanceOf(InvalidTimeoutConfigurationException.class)
                .hasMessageContaining("Response timeout must be positive");
    }

    @Test
    void etaWebClient_withValidTimeout_doesNotThrow() {
        assertThatCode(() -> CONFIG.etaWebClient(
                WebClient.builder(),
                configWithTimeout(10),
                false
        )).doesNotThrowAnyException();
    }
}