// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.permission.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

/**
 * Accepted event for AccountingPointDataNeeds
 */
@Entity
public class EsAcceptedEventForAPD extends PersistablePermissionEvent {
    public EsAcceptedEventForAPD(String permissionId) {
        super(permissionId, PermissionProcessStatus.ACCEPTED);
    }

    protected EsAcceptedEventForAPD() {
        super();
    }
}
