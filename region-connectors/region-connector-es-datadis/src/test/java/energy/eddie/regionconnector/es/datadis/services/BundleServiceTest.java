// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.services;

import energy.eddie.regionconnector.es.datadis.DatadisPermissionRequestBuilder;
import energy.eddie.regionconnector.es.datadis.api.AuthorizationApi;
import energy.eddie.regionconnector.es.datadis.dtos.AuthorizationRequest;
import energy.eddie.regionconnector.es.datadis.dtos.AuthorizationRequestFactory;
import energy.eddie.regionconnector.es.datadis.dtos.AuthorizationRequestResponse;
import energy.eddie.regionconnector.es.datadis.permission.events.EsSentToPermissionAdministratorEvent;
import energy.eddie.regionconnector.es.datadis.permission.events.EsSimpleEvent;
import energy.eddie.regionconnector.es.datadis.persistence.EsPermissionRequestRepository;
import energy.eddie.regionconnector.shared.event.sourcing.Outbox;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static energy.eddie.regionconnector.es.datadis.DatadisRegionConnectorMetadata.ZONE_ID_SPAIN;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BundleServiceTest {
    @Mock
    private EsPermissionRequestRepository repository;
    @Mock
    private AuthorizationRequestFactory factory;
    @Mock
    private AuthorizationApi api;
    @Mock
    private Outbox outbox;
    @Mock
    @SuppressWarnings("unused")
    private EntityManager em;
    @InjectMocks
    private BundleService bundleService;

    @Test
    void givenEmptyBundle_whenSendBundledAuthorizationRequest_thenDoNothing() {
        // Given
        var bundleId = UUID.randomUUID();
        when(repository.findAllByBundleId(bundleId)).thenReturn(List.of());

        // When
        bundleService.sendBundledAuthorizationRequest(bundleId);

        // Then
        verifyNoInteractions(factory, api, outbox);
    }


    @Test
    void givenBundleWithMultiplePermissionRequests_whenSendBundledAuthorizationRequest_thenSendsAuthorizationRequest() {
        // Given
        var today = LocalDate.now(ZONE_ID_SPAIN);
        var yesterday = today.minusDays(1);
        var bundleId = UUID.randomUUID();
        var pr1 = new DatadisPermissionRequestBuilder()
                .setPermissionId("pid1")
                .setBundleId(bundleId)
                .setNif("nif")
                .setMeteringPointId("mid")
                .setEnd(yesterday)
                .build();
        var pr2 = new DatadisPermissionRequestBuilder()
                .setPermissionId("pid2")
                .setBundleId(bundleId)
                .setNif("nif")
                .setMeteringPointId("mid")
                .setEnd(today)
                .build();
        when(repository.findAllByBundleId(bundleId)).thenReturn(List.of(pr1, pr2));
        var request = new AuthorizationRequest(today, today, "nif", List.of("mid"));
        when(factory.from("nif", "mid", today))
                .thenReturn(request);
        when(api.postAuthorizationRequest(request))
                .thenReturn(Mono.just(AuthorizationRequestResponse.fromResponse("ok")));

        // When
        bundleService.sendBundledAuthorizationRequest(bundleId);

        // Then
        verify(outbox, times(2)).commit(any(EsSentToPermissionAdministratorEvent.class));
    }


    @Test
    void givenExceptionDuringAuthorization_whenSendBundledAuthorizationRequest_thenEmitsUnableToSend() {
        // Given
        var today = LocalDate.now(ZONE_ID_SPAIN);
        var bundleId = UUID.randomUUID();
        var pr = new DatadisPermissionRequestBuilder()
                .setPermissionId("pid")
                .setBundleId(bundleId)
                .setNif("nif")
                .setMeteringPointId("mid")
                .setEnd(today)
                .build();
        when(repository.findAllByBundleId(bundleId)).thenReturn(List.of(pr));
        var request = new AuthorizationRequest(today, today, "nif", List.of("mid"));
        when(factory.from("nif", "mid", today))
                .thenReturn(request);
        when(api.postAuthorizationRequest(request))
                .thenReturn(Mono.error(new RuntimeException()));

        // When
        bundleService.sendBundledAuthorizationRequest(bundleId);

        // Then
        verify(outbox).commit(any(EsSimpleEvent.class));
    }
}