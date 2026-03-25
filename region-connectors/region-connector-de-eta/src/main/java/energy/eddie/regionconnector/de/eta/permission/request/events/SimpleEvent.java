// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.de.eta.permission.request.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

/**
 * Simple permission event for the German (DE) region connector.
 * This event is used for basic status changes.
 */
@Entity(name = "DeSimpleEvent")
@SuppressWarnings("NullAway") // Needed for JPA
public class SimpleEvent extends PersistablePermissionEvent {
    
    public SimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected SimpleEvent() {
        super();
    }
}
