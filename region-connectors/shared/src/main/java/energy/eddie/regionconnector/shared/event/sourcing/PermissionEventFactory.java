// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.shared.event.sourcing;

import energy.eddie.api.agnostic.process.model.events.PermissionEvent;
import energy.eddie.cim.agnostic.PermissionProcessStatus;

@FunctionalInterface
public interface PermissionEventFactory {
    PermissionEvent create(String permissionId, PermissionProcessStatus status);
}
