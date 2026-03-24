// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
@SuppressWarnings("NullAway") // Needed for JPA
public class SimpleEvent extends PersistablePermissionEvent {
    public SimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected SimpleEvent() {
        super();
    }
}
