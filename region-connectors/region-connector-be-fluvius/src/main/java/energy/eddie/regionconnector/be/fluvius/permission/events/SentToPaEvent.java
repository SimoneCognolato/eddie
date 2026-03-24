// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.be.fluvius.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
@SuppressWarnings({"NullAway", "unused"})
public class SentToPaEvent extends PersistablePermissionEvent {
    private final String shortUrlIdentifier;

    public SentToPaEvent(String permissionId, String shortUrlIdentifier) {
        super(permissionId, PermissionProcessStatus.SENT_TO_PERMISSION_ADMINISTRATOR);
        this.shortUrlIdentifier = shortUrlIdentifier;
    }

    protected SentToPaEvent() {
        shortUrlIdentifier = null;
    }
}
