// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.permission.events;

import energy.eddie.api.v0.PermissionProcessStatus;
import energy.eddie.regionconnector.es.datadis.dtos.AllowedGranularity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@SuppressWarnings({"NullAway", "unused"})
public class EsValidatedEvent extends PersistablePermissionEvent {
    @Column(name = "permission_start")
    private final LocalDate start;
    @Column(name = "permission_end")
    private final LocalDate end;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text", name = "allowed_granularity")
    private final AllowedGranularity allowedGranularity;
    @Nullable
    @Column(name = "bundle_id")
    private final UUID bundleId;

    public EsValidatedEvent(
            String permissionId,
            LocalDate start,
            LocalDate end,
            @Nullable AllowedGranularity allowedGranularity,
            @Nullable UUID bundleId
    ) {
        super(permissionId, PermissionProcessStatus.VALIDATED);
        this.start = start;
        this.end = end;
        this.allowedGranularity = allowedGranularity;
        this.bundleId = bundleId;
    }

    protected EsValidatedEvent() {
        super();
        start = null;
        end = null;
        allowedGranularity = null;
        bundleId = null;
    }

    public LocalDate end() {
        return end;
    }

    @Nullable
    public UUID bundleId() {
        return bundleId;
    }
}
