// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.core.services;

import energy.eddie.api.agnostic.RawDataProvider;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.RawDataMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.mock;

class RawDataServiceTest {
    @BeforeAll
    static void beforeAll() {
        StepVerifier.setDefaultTimeout(Duration.ofSeconds(2));
    }

    @Test
    void givenMultipleStreams_combinesAndEmitsAllValuesFromAllStreams() {
        // Given
        RawDataService rawDataService = new RawDataService();
        Sinks.Many<RawDataMessage> sink1 = Sinks.many().unicast().onBackpressureBuffer();
        Sinks.Many<RawDataMessage> sink2 = Sinks.many().unicast().onBackpressureBuffer();

        RawDataProvider provider1 = createProvider(sink1);
        RawDataProvider provider2 = createProvider(sink2);

        // When
        var flux = rawDataService.getRawDataStream();
        StepVerifier.create(flux)
                    .then(() -> {
                        rawDataService.registerProvider(provider1);
                        sink1.tryEmitNext(new RawDataMessage().withPermissionId("foo")
                                                              .withConnectionId("bar")
                                                              .withDataNeedId("id1")
                                                              .withDataSourceInformation(mock(DataSourceInformation.class))
                                                              .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                                              .withRawPayload("rawPayload1"));
                        sink1.tryEmitNext(new RawDataMessage().withPermissionId("foo")
                                                              .withConnectionId("bar")
                                                              .withDataNeedId("id1")
                                                              .withDataSourceInformation(mock(DataSourceInformation.class))
                                                              .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                                              .withRawPayload("rawPayload2"));
                    })
                    // Then
                    .expectNextCount(2)
                    // When
                    .then(() -> {
                        rawDataService.registerProvider(provider2);
                        sink1.tryEmitNext(new RawDataMessage().withPermissionId("foo")
                                                              .withConnectionId("bar")
                                                              .withDataNeedId("id1")
                                                              .withDataSourceInformation(mock(DataSourceInformation.class))
                                                              .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                                              .withRawPayload("rawPayload3"));
                        sink1.tryEmitNext(new RawDataMessage().withPermissionId("foo")
                                                              .withConnectionId("bar")
                                                              .withDataNeedId("id1")
                                                              .withDataSourceInformation(mock(DataSourceInformation.class))
                                                              .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                                              .withRawPayload("rawPayload4"));
                    })
                    // Then
                    .expectNextCount(2)
                    .thenCancel()
                    .verify();

        provider1.close();
        provider2.close();
    }

    private static RawDataProvider createProvider(Sinks.Many<RawDataMessage> sink) {
        return new RawDataProvider() {
            @Override
            public Flux<RawDataMessage> getRawDataStream() {
                return sink.asFlux();
            }

            @Override
            public void close() {
                sink.tryEmitComplete();
            }
        };
    }
}
