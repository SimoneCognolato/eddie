// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.dk.energinet.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
@SuppressWarnings({"NullAway", "unused"})
public class DkSimpleEvent extends PersistablePermissionEvent {
    public DkSimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected DkSimpleEvent() {}
}
