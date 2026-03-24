// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.nl.mijn.aansluiting.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity
public class NlSimpleEvent extends NlPermissionEvent {
    public NlSimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected NlSimpleEvent() {
        super();
    }
}
