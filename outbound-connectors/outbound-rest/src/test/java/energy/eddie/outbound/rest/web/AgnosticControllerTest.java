// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.outbound.rest.web;

import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.OpaqueEnvelope;
import energy.eddie.cim.agnostic.RawDataMessage;
import energy.eddie.cim.agnostic.Status;
import energy.eddie.outbound.rest.RestTestConfig;
import energy.eddie.outbound.rest.connectors.AgnosticConnector;
import energy.eddie.outbound.rest.model.ConnectionStatusMessageModel;
import energy.eddie.outbound.rest.model.RawDataMessageModel;
import energy.eddie.outbound.rest.persistence.ConnectionStatusMessageRepository;
import energy.eddie.outbound.rest.persistence.RawDataMessageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.web.reactive.ReactiveWebSecurityAutoConfiguration;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@WebFluxTest(value = AgnosticController.class, excludeAutoConfiguration = ReactiveWebSecurityAutoConfiguration.class)
@Import({WebTestConfig.class, AgnosticConnector.class, RestTestConfig.class})
class AgnosticControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AgnosticConnector agnosticConnector;
    @MockitoBean
    private ConnectionStatusMessageRepository csmRepository;
    @MockitoBean
    private RawDataMessageRepository rawDataRepository;

    @Test
    @DirtiesContext
    void connectionStatusMessageSSE_returnsMessages() {
        var message1 = statusMessage(Status.CREATED);
        var message2 = statusMessage(Status.VALIDATED);

        agnosticConnector.setConnectionStatusMessageStream(Flux.just(message1, message2));

        var result = webTestClient.get()
                                  .uri("/agnostic/connection-status-messages")
                                  .accept(MediaType.TEXT_EVENT_STREAM)
                                  .exchange()
                                  .expectStatus()
                                  .isOk()
                                  .returnResult(ConnectionStatusMessage.class)
                                  .getResponseBody();

        StepVerifier.create(result)
                    .then(agnosticConnector::close)
                    .expectNextMatches(message -> message1.getStatus().equals(message.getStatus()))
                    .expectNextMatches(message -> message2.getStatus().equals(message.getStatus()))
                    .verifyComplete();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void connectionStatusMessage_returnsMessages() {
        var msg = new ConnectionStatusMessageModel(statusMessage(Status.CREATED));
        given(csmRepository.findAll(ArgumentMatchers.<PredicateSpecification<ConnectionStatusMessageModel>>any()))
                .willReturn(List.of(msg));


        var result = webTestClient.get()
                                  .uri("/agnostic/connection-status-messages")
                                  .accept(MediaType.APPLICATION_JSON)
                                  .exchange()
                                  .expectStatus()
                                  .isOk()
                                  .returnResult(new ParameterizedTypeReference<List<ConnectionStatusMessage>>() {})
                                  .getResponseBody();

        StepVerifier.create(result)
                    .assertNext(next -> {
                        assertNotNull(next.getFirst());
                        assertEquals(msg.payload().getStatus(), next.getFirst().getStatus());
                    })
                    .verifyComplete();
    }

    @Test
    @DirtiesContext
    void rawDataMessageSSE_returnsMessages() {
        var message1 = new RawDataMessage().withPermissionId("pid")
                                           .withConnectionId("cid")
                                           .withDataNeedId("dnid")
                                           .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                           .withRawPayload("{}");
        var message2 = new RawDataMessage().withPermissionId("other-pid")
                                           .withConnectionId("cid")
                                           .withDataNeedId("dnid")
                                           .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                           .withRawPayload("[]");

        agnosticConnector.setRawDataStream(Flux.just(message1, message2));

        var result = webTestClient.get()
                                  .uri("/agnostic/raw-data-messages")
                                  .accept(MediaType.TEXT_EVENT_STREAM)
                                  .exchange()
                                  .expectStatus()
                                  .isOk()
                                  .returnResult(RawDataMessage.class)
                                  .getResponseBody();

        StepVerifier.create(result)
                    .then(agnosticConnector::close)
                    .expectNextMatches(message -> message1.getPermissionId().equals(message.getPermissionId()))
                    .expectNextMatches(message -> message2.getPermissionId().equals(message.getPermissionId()))
                    .verifyComplete();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void rawDataMessage_returnsMessages() {
        var message = new RawDataMessage().withPermissionId("pid")
                                          .withConnectionId("cid")
                                          .withDataNeedId("dnid")
                                          .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                          .withRawPayload("{}");
        var msg = new RawDataMessageModel(message);
        given(rawDataRepository.findAll(ArgumentMatchers.<PredicateSpecification<RawDataMessageModel>>any()))
                .willReturn(List.of(msg));


        var result = webTestClient.get()
                                  .uri("/agnostic/raw-data-messages")
                                  .accept(MediaType.APPLICATION_JSON)
                                  .exchange()
                                  .expectStatus()
                                  .isOk()
                                  .returnResult(new ParameterizedTypeReference<List<RawDataMessage>>() {})
                                  .getResponseBody();

        StepVerifier.create(result)
                    .assertNext(next -> {
                        assertNotNull(next.getFirst());
                        assertEquals(msg.payload().getPermissionId(), next.getFirst().getPermissionId());
                    })
                    .verifyComplete();
    }

    @Test
    void opaqueEnvelope_returnsAccepted() {
        var id = "test-id";
        var envelope = new OpaqueEnvelope().withConnectionId(id)
                                           .withPermissionId(id)
                                           .withDataNeedId(id)
                                           .withMessageId(id)
                                           .withRegionConnectorId(id)
                                           .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                           .withPayload("test-payload");

        webTestClient.post()
                     .uri("/agnostic/opaque-envelope")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(envelope)
                     .exchange()
                     .expectStatus()
                     .isAccepted();
    }

    private ConnectionStatusMessage statusMessage(Status status) {
        return new ConnectionStatusMessage().withStatus(status);
    }
}