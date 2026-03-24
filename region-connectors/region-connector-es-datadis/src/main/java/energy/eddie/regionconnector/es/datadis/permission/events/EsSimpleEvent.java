// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

@Entity

public class EsSimpleEvent extends PersistablePermissionEvent {
    public EsSimpleEvent(String permissionId, PermissionProcessStatus status) {
        super(permissionId, status);
    }

    protected EsSimpleEvent() {
        super();
    }
}
