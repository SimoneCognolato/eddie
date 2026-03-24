// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.us.green.button.permission.events;

import energy.eddie.api.agnostic.process.model.events.InternalPermissionEvent;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

/**
 * Used to indicate that the authorization was updated and should include meters now.
 */
@Entity
@SuppressWarnings("NullAway")
public class UsAuthorizationUpdateFinishedEvent extends PersistablePermissionEvent implements InternalPermissionEvent {
    public UsAuthorizationUpdateFinishedEvent(String permissionId) {
        super(permissionId, PermissionProcessStatus.ACCEPTED);
    }

    protected UsAuthorizationUpdateFinishedEvent() {}
}
