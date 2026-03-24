// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.fr.enedis.permission.events;

import energy.eddie.api.agnostic.process.model.events.InternalPermissionEvent;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@SuppressWarnings({"NullAway", "unused"})
public class FrInternalPollingEvent extends PersistablePermissionEvent implements InternalPermissionEvent {
    private final LocalDate latestMeterReadingEndDate;

    public FrInternalPollingEvent(String permissionId, LocalDate latestMeterReadingEndDate) {
        super(permissionId, PermissionProcessStatus.ACCEPTED);
        this.latestMeterReadingEndDate = latestMeterReadingEndDate;
    }

    protected FrInternalPollingEvent() {
        super();
        latestMeterReadingEndDate = null;
    }
}
