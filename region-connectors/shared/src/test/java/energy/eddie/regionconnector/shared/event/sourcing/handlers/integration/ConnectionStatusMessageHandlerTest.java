// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.shared.event.sourcing.handlers.integration;


import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.api.agnostic.process.model.persistence.PermissionRequestRepository;
import energy.eddie.api.v0.PermissionProcessStatus;
import energy.eddie.cim.agnostic.KeyValuePair;
import energy.eddie.cim.agnostic.Status;
import energy.eddie.regionconnector.shared.event.sourcing.EventBus;
import energy.eddie.regionconnector.shared.event.sourcing.EventBusImpl;
import energy.eddie.regionconnector.shared.permission.requests.SimplePermissionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SuppressWarnings("resource")
@ExtendWith(MockitoExtension.class)
class ConnectionStatusMessageHandlerTest {
    @Mock
    private PermissionRequestRepository<PermissionRequest> repository;

    @Test
    void testAccept_emitsStatusMessage() {
        // Given
        var permissionRequest = new SimplePermissionRequest("pid", "cid", "dnid", PermissionProcessStatus.VALIDATED);
        when(repository.findByPermissionId("pid")).thenReturn(Optional.of(permissionRequest));
        EventBusImpl eventBus = new EventBusImpl();
        var handler = new ConnectionStatusMessageHandler<>(eventBus, repository, pr -> "");

        // When
        eventBus.emit(new SimpleEvent("pid", PermissionProcessStatus.VALIDATED));

        // Then
        StepVerifier.create(handler.getConnectionStatusMessageStream())
                    .then(handler::close)
                    .assertNext(csm -> assertAll(
                            () -> assertEquals(permissionRequest.connectionId(), csm.getConnectionId()),
                            () -> assertEquals(permissionRequest.permissionId(), csm.getPermissionId()),
                            () -> assertEquals(permissionRequest.dataNeedId(), csm.getDataNeedId()),
                            () -> assertEquals(Status.VALIDATED, csm.getStatus()),
                            () -> assertEquals("", csm.getMessage())
                    ))
                    .verifyComplete();
    }

    @Test
    void testAccept_doesNotEmitStatus_ifNoPermissionIsFound() {
        // Given
        when(repository.findByPermissionId("pid")).thenReturn(Optional.empty());
        EventBus eventBus = new EventBusImpl();
        var handler = new ConnectionStatusMessageHandler<>(eventBus, repository, pr -> "");

        // When
        eventBus.emit(new SimpleEvent("pid", PermissionProcessStatus.VALIDATED));

        // Then
        StepVerifier.create(handler.getConnectionStatusMessageStream())
                    .then(handler::close)
                    .verifyComplete();
    }

    @Test
    void testAccept_doesNotEmitStatus_onInternalEvents() {
        // Given
        EventBus eventBus = new EventBusImpl();
        var handler = new ConnectionStatusMessageHandler<>(eventBus, repository, pr -> "");

        // When
        eventBus.emit(new InternalEvent("pid", PermissionProcessStatus.VALIDATED));

        // Then
        StepVerifier.create(handler.getConnectionStatusMessageStream())
                    .then(handler::close)
                    .verifyComplete();
    }

    @Test
    void testAccept_emitsExtension() {
        // Given
        var permissionRequest = new SimplePermissionRequest("pid", "cid", "dnid", PermissionProcessStatus.VALIDATED);
        when(repository.findByPermissionId("pid")).thenReturn(Optional.of(permissionRequest));
        EventBus eventBus = new EventBusImpl();
        var keyValuePair = new KeyValuePair().withKey("key")
                                             .withValue("value");
        var handler = new ConnectionStatusMessageHandler<>(eventBus,
                                                           repository,
                                                           pr -> "",
                                                           pr -> List.of(keyValuePair));

        // When
        eventBus.emit(new SimpleEvent("pid", PermissionProcessStatus.VALIDATED));

        // Then
        StepVerifier.create(handler.getConnectionStatusMessageStream())
                    .then(handler::close)
                    .assertNext(csm -> assertEquals(keyValuePair,
                                                    Objects.requireNonNull(csm.getExtensions())
                                                           .getFirst()))
                    .verifyComplete();
    }
}
