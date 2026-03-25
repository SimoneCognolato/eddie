// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.us.green.button.permission.events;

import energy.eddie.api.agnostic.process.model.events.InternalPermissionEvent;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
public class UsStartPollingEvent extends PersistablePermissionEvent implements InternalPermissionEvent {
    public UsStartPollingEvent(String permissionId) {
        super(permissionId, PermissionProcessStatus.ACCEPTED);
    }

    protected UsStartPollingEvent() {
        super();
    }
}
