// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.cds.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity(name = "CdsSimpleEvent")
public class SimpleEvent extends PersistablePermissionEvent {
    public SimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected SimpleEvent() {}
}
