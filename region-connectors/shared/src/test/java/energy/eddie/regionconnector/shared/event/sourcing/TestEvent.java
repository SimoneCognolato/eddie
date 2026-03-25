// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.shared.event.sourcing;

import energy.eddie.api.agnostic.process.model.events.PermissionEvent;
import energy.eddie.cim.agnostic.PermissionProcessStatus;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public record TestEvent(String permissionId, PermissionProcessStatus status,
                 ZonedDateTime eventCreated) implements PermissionEvent {
    public TestEvent(String permissionId, PermissionProcessStatus status) {
        this(permissionId, status, ZonedDateTime.now(ZoneOffset.UTC));
    }

}
