// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.core.services;

import energy.eddie.api.agnostic.ConnectionStatusMessageProvider;
import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.Mockito.mock;

class PermissionServiceTest {

    @BeforeAll
    static void beforeAll() {
        StepVerifier.setDefaultTimeout(Duration.ofSeconds(2));
    }

    @Test
    void givenMultipleStreams_combinesAndEmitsAllValuesFromAllStreams() {
        // Given
        PermissionService service = new PermissionService();
        Sinks.Many<ConnectionStatusMessage> sink1 = Sinks.many().unicast().onBackpressureBuffer();
        Sinks.Many<ConnectionStatusMessage> sink2 = Sinks.many().unicast().onBackpressureBuffer();

        ConnectionStatusMessageProvider provider1 = createProvider(sink1);
        ConnectionStatusMessageProvider provider2 = createProvider(sink2);

        // When
        var flux = service.getConnectionStatusMessageStream();
        StepVerifier.create(flux)
                    .then(() -> {
                        service.registerProvider(provider1);

                        sink1.tryEmitNext(new ConnectionStatusMessage().withConnectionId("one")
                                                                       .withPermissionId("one")
                                                                       .withDataNeedId("one")
                                                                       .withDataSourceInformation(mock(
                                                                               DataSourceInformation.class))
                                                                       .withStatus(Status.CREATED));
                        sink1.tryEmitNext(new ConnectionStatusMessage().withConnectionId("three")
                                                                       .withPermissionId("three")
                                                                       .withDataNeedId("three")
                                                                       .withDataSourceInformation(mock(
                                                                               DataSourceInformation.class))
                                                                       .withStatus(Status.INVALID));
                    })
                    // Then
                    .expectNextCount(2)
                    // When
                    .then(() -> {
                        service.registerProvider(provider2);

                        sink2.tryEmitNext(new ConnectionStatusMessage().withConnectionId("two")
                                                                       .withPermissionId("two")
                                                                       .withDataNeedId("two")
                                                                       .withDataSourceInformation(mock(
                                                                               DataSourceInformation.class))
                                                                       .withStatus(Status.VALIDATED));
                        sink1.tryEmitNext(new ConnectionStatusMessage().withConnectionId("four")
                                                                       .withPermissionId("four")
                                                                       .withDataNeedId("four")
                                                                       .withDataSourceInformation(mock(
                                                                               DataSourceInformation.class))
                                                                       .withStatus(Status.INVALID));
                    })
                    .expectNextCount(2)
                    // Then
                    .thenCancel()
                    .verify();
    }

    private static ConnectionStatusMessageProvider createProvider(Sinks.Many<ConnectionStatusMessage> sink) {
        return new ConnectionStatusMessageProvider() {
            @Override
            public Flux<ConnectionStatusMessage> getConnectionStatusMessageStream() {
                return sink.asFlux();
            }

            @Override
            public void close() throws Exception {
                sink.tryEmitComplete();
            }
        };
    }
}
