// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.us.green.button.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
public class UsSimpleEvent extends PersistablePermissionEvent {
    public UsSimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected UsSimpleEvent() {
        super();
    }
}
