// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.shared.event.sourcing.handlers.integration;

import energy.eddie.api.agnostic.ConnectionStatusMessageProvider;
import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.api.agnostic.process.model.events.InternalPermissionEvent;
import energy.eddie.api.agnostic.process.model.events.PermissionEvent;
import energy.eddie.api.agnostic.process.model.persistence.PermissionRequestRepository;
import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.KeyValuePair;
import energy.eddie.regionconnector.shared.event.sourcing.EventBus;
import energy.eddie.regionconnector.shared.event.sourcing.handlers.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * Subscribes to all events of an {@code EventBus} and creates connection status messages based on an event.
 */
public class ConnectionStatusMessageHandler<T extends PermissionRequest> implements EventHandler<PermissionEvent>, ConnectionStatusMessageProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionStatusMessageHandler.class);
    private final Sinks.Many<ConnectionStatusMessage> messages = Sinks.many().multicast().onBackpressureBuffer();
    private final PermissionRequestRepository<T> repository;
    private final Function<T, String> messageFunc;
    private final Function<T, List<KeyValuePair>> extensionFunc;

    public ConnectionStatusMessageHandler(
            EventBus eventBus,
            PermissionRequestRepository<T> repository,
            Function<T, String> messageFunc
    ) {
        this(eventBus, repository, messageFunc, permissionRequest -> null);
    }

    public ConnectionStatusMessageHandler(
            EventBus eventBus,
            PermissionRequestRepository<T> repository,
            Function<T, String> messageFunc,
            Function<T, List<KeyValuePair>> extensionFunc
    ) {
        this.repository = repository;
        this.messageFunc = messageFunc;
        this.extensionFunc = extensionFunc;
        eventBus.filteredFlux(PermissionEvent.class)
                .subscribe(this::accept);
    }

    @Override
    public void accept(PermissionEvent permissionEvent) {
        var permissionId = permissionEvent.permissionId();
        var status = permissionEvent.status();
        LOGGER.trace("Received permission event: {} with status {}", permissionId, status);
        if (permissionEvent instanceof InternalPermissionEvent) {
            LOGGER.debug("Ignoring internal permission event {} with status {}", permissionId, status);
            return;
        }
        var optionalRequest = repository.findByPermissionId(permissionId);
        if (optionalRequest.isEmpty()) {
            LOGGER.warn("Got event without permission request for permission id {} with status {}", permissionId,
                        status);
            return;
        }
        LOGGER.trace("Publishing connection status message for permission id {} with status {}", permissionId, status);
        var permissionRequest = optionalRequest.get();

        messages.tryEmitNext(
                new ConnectionStatusMessage().withConnectionId(permissionRequest.connectionId())
                                             .withPermissionId(permissionId)
                                             .withDataNeedId(permissionRequest.dataNeedId())
                                             .withDataSourceInformation(permissionRequest.dataSourceInformation())
                                             .withStatus(status.toAgnosticDto())
                                             .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                             .withMessage(messageFunc.apply(permissionRequest))
                                             .withExtensions(extensionFunc.apply(permissionRequest))
        );
    }

    @Override
    public Flux<ConnectionStatusMessage> getConnectionStatusMessageStream() {
        return messages.asFlux();
    }

    @Override
    public void close() {
        messages.tryEmitComplete();
    }
}
