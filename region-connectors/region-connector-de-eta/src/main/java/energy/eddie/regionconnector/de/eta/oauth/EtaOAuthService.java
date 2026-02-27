package energy.eddie.regionconnector.de.eta.oauth;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import energy.eddie.regionconnector.de.eta.config.DeEtaPlusConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@Service
public class EtaOAuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtaOAuthService.class);

    private final DeEtaPlusConfiguration configuration;

    public EtaOAuthService(DeEtaPlusConfiguration configuration) {
        this.configuration = configuration;
    }

    public Mono<OAuthTokenResponse> exchangeCodeForToken(String code, String openid) {
        return Mono.fromCallable(() -> {
                       LOGGER.info("Exchanging authorization token for access token");

                       URI tokenEndpoint = UriComponentsBuilder
                               .fromUriString(configuration.oauth().tokenUrl())
                               .queryParam("token", code)
                               .queryParam("openid", openid)
                               .queryParam("client_id", configuration.oauth().clientId())
                               .build()
                               .toUri();

                       HTTPRequest request = new HTTPRequest(HTTPRequest.Method.PUT, tokenEndpoint);
                       request.setAccept("application/json");

                       HTTPResponse response = request.send();

                       if (!response.indicatesSuccess()) {
                           LOGGER.warn("Token exchange returned unsuccessful response: {}", response.getContent());
                           return new OAuthTokenResponse(null, false);
                       }

                       var jsonObject = response.getContentAsJSONObject();

                       boolean success = false;
                       if (jsonObject.containsKey("success") && jsonObject.get("success") instanceof Boolean) {
                           success = (Boolean) jsonObject.get("success");
                       }

                       if (!success) {
                           LOGGER.warn("Token exchange returned unsuccessful response: success flag is false");
                           return new OAuthTokenResponse(null, false);
                       }

                       @SuppressWarnings("unchecked")
                       var data = (java.util.Map<String, Object>) jsonObject.get("data");

                       String token = null;
                       String refreshTokenString = null;

                       if (data != null) {
                           token = (String) data.get("token");
                           refreshTokenString = (String) data.get("refreshToken");
                       }

                       if (token == null) {
                           LOGGER.warn("Token exchange returned a response without a token");
                           return new OAuthTokenResponse(null, false);
                       }
                       LOGGER.info("Successfully exchanged token for access token");
                       return new OAuthTokenResponse(new OAuthTokenResponse.TokenData(token, refreshTokenString), success);
                   }).subscribeOn(Schedulers.boundedElastic())
                   .onErrorResume(error -> {
                       LOGGER.error("Error during token exchange", error);
                       return Mono.just(new OAuthTokenResponse(null, false));
                   });
    }
}
