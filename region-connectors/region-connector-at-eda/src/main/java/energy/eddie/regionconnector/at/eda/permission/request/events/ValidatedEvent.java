// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request.events;

import energy.eddie.api.agnostic.data.needs.EnergyDirection;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.regionconnector.at.eda.requests.restricted.enums.AllowedGranularity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@SuppressWarnings("NullAway") // Needed for JPA
public class ValidatedEvent extends PersistablePermissionEvent {
    private final LocalDate permissionStart;
    private final LocalDate permissionEnd;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private final AllowedGranularity granularity;
    private final String cmRequestId;
    private final String conversationId;
    @Enumerated(EnumType.STRING)
    private final EnergyDirection energyDirection;
    private final Integer participationFactor;
    @Transient
    private NeedsToBeSent needsToBeSent = NeedsToBeSent.NO;

    public ValidatedEvent() {
        super();
        permissionStart = null;
        permissionEnd = null;
        granularity = null;
        cmRequestId = null;
        conversationId = null;
        energyDirection = null;
        participationFactor = null;
    }

    @SuppressWarnings("java:S107")
    // The validated event requires many attributes, since they should only be set after a permission request has been validated
    public ValidatedEvent(
            String permissionId,
            LocalDate permissionStart,
            @Nullable
            LocalDate permissionEnd,
            @Nullable
            AllowedGranularity granularity,
            String cmRequestId,
            String conversationId,
            @Nullable EnergyDirection energyDirection,
            @Nullable Integer participationFactor,
            NeedsToBeSent needsToBeSent
    ) {
        super(permissionId, PermissionProcessStatus.VALIDATED);
        this.permissionStart = permissionStart;
        this.permissionEnd = permissionEnd;
        this.granularity = granularity;
        this.cmRequestId = cmRequestId;
        this.conversationId = conversationId;
        this.energyDirection = energyDirection;
        this.participationFactor = participationFactor;
        this.needsToBeSent = needsToBeSent;
    }

    public ValidatedEvent(
            String permissionId,
            LocalDate permissionStart,
            @Nullable
            LocalDate permissionEnd,
            @Nullable
            AllowedGranularity granularity,
            String cmRequestId,
            String conversationId,
            NeedsToBeSent needsToBeSent
    ) {
        super(permissionId, PermissionProcessStatus.VALIDATED);
        this.permissionStart = permissionStart;
        this.permissionEnd = permissionEnd;
        this.granularity = granularity;
        this.cmRequestId = cmRequestId;
        this.conversationId = conversationId;
        this.energyDirection = null;
        this.participationFactor = null;
        this.needsToBeSent = needsToBeSent;
    }

    public LocalDate start() {
        return permissionStart;
    }

    @Nullable
    public LocalDate end() {
        return permissionEnd;
    }

    @Nullable
    public AllowedGranularity granularity() {
        return granularity;
    }

    public String cmRequestId() {
        return cmRequestId;
    }

    public String conversationId() {
        return conversationId;
    }

    @Nullable
    public EnergyDirection energyDirection() {
        return energyDirection;
    }

    @Nullable
    public Integer participationFactor() {
        return participationFactor;
    }

    public boolean needsToBeSent() {
        return needsToBeSent == NeedsToBeSent.YES;
    }

    public enum NeedsToBeSent {
        YES,
        NO
    }
}
