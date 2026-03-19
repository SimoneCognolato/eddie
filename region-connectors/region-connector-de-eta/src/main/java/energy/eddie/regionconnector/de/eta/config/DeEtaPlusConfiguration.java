package energy.eddie.regionconnector.de.eta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration for the German (DE) ETA Plus region connector.
 */
@ConfigurationProperties("region-connector.de.eta")
public record DeEtaPlusConfiguration(
                String eligiblePartyId,
                @DefaultValue("https://eta-plus.com") String apiBaseUrl,
                String apiClientId,
                String apiClientSecret,
                @DefaultValue("/api/meters/historical") String meteredDataEndpoint,
                @DefaultValue("/api/v1/permissions/{id}") String permissionCheckEndpoint,
                @DefaultValue("30") int responseTimeoutSeconds,
                AuthConfig auth,
                ApiConfig api) {

        public record AuthConfig(
                        String clientId,
                        String clientSecret,
                        String tokenUrl,
                        String authorizationUrl,
                        String redirectUri,
                        String scope) {
        }

        public record ApiConfig(ClientConfig client) {
                public record ClientConfig(String id, String secret) {
                }
        }
}
