// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.core.services.v1_12;

import energy.eddie.cim.v1_12.ack.AcknowledgementEnvelope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
class AcknowledgementMarketDocumentServiceTest {
    @BeforeAll
    static void beforeAll() {
        StepVerifier.setDefaultTimeout(Duration.ofSeconds(2));
    }

    @Test
    void givenMultipleStreams_combinesAndEmitsAllValuesFromAllStreams() {
        // Given
        var service = new AcknowledgementMarketDocumentService();
        Sinks.Many<AcknowledgementEnvelope> sink1 = Sinks.many().unicast().onBackpressureBuffer();
        Sinks.Many<AcknowledgementEnvelope> sink2 = Sinks.many().unicast().onBackpressureBuffer();

        service.registerProvider(sink1::asFlux);
        service.registerProvider(sink2::asFlux);

        var one = new AcknowledgementEnvelope();
        var two = new AcknowledgementEnvelope();
        var three = new AcknowledgementEnvelope();

        // When
        var flux = service.getAcknowledgementMarketDocumentStream();
        StepVerifier.create(flux)
                    .then(() -> {
                        sink2.tryEmitNext(two);
                        sink1.tryEmitNext(one);
                        sink2.tryEmitNext(three);
                    })
                    // Then
                    .expectNextCount(3)
                    .thenCancel()
                    .verify();
    }

    @Test
    void givenConverter_appliesItToStream() {
        // Given
        var service = new AcknowledgementMarketDocumentService();
        Sinks.Many<AcknowledgementEnvelope> sink = Sinks.many().unicast().onBackpressureBuffer();

        service.registerProvider(sink::asFlux);

        var one = new AcknowledgementEnvelope();
        // When
        var flux = service.getAcknowledgementMarketDocumentStream();
        StepVerifier.create(flux)
                    .then(() -> sink.tryEmitNext(one))
                    // Then
                    .expectNextCount(1)
                    .thenCancel()
                    .verify();
    }
}