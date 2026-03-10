// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.spring.regionconnector.extensions.agnostic;

import energy.eddie.api.agnostic.opaque.RegionConnectorOpaqueEnvelopeService;
import energy.eddie.core.services.agnostic.OpaqueEnvelopeRouter;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OpaqueEnvelopeRouterRegistrarTest {
    @Test
    void givenNull_constructor_throws() {
        // Given
        Optional<RegionConnectorOpaqueEnvelopeService> emptyService = Optional.empty();
        Optional<OpaqueEnvelopeRouter> emptyRouter = Optional.empty();

        // When, Then
        assertThrows(NullPointerException.class,
                     () -> new OpaqueEnvelopeRouterRegistrar(null, emptyService, emptyRouter));
        assertThrows(NullPointerException.class, () -> new OpaqueEnvelopeRouterRegistrar("test", null, emptyRouter));
        assertThrows(NullPointerException.class, () -> new OpaqueEnvelopeRouterRegistrar("test", emptyService, null));
    }

    @Test
    void givenRegionConnector_registersAtService() {
        // Given
        var service = mock(RegionConnectorOpaqueEnvelopeService.class);
        var router = mock(OpaqueEnvelopeRouter.class);

        // When
        new OpaqueEnvelopeRouterRegistrar("test", Optional.of(service), Optional.of(router));

        // Then
        verify(router).registerOpaqueEnvelopeService("test", service);
    }
}