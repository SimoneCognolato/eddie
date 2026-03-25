// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request;

import energy.eddie.api.agnostic.data.needs.EnergyDirection;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.regionconnector.at.eda.requests.restricted.enums.AllowedGranularity;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class EdaPermissionRequestBuilder {
    private String connectionId = null;
    private String permissionId = null;
    private String dataNeedId = null;
    private String cmRequestId = null;
    private String conversationId = null;
    private String meteringPointId = null;
    private String dsoId = null;
    private LocalDate start = null;
    private LocalDate end = null;
    private AllowedGranularity granularity = null;
    private PermissionProcessStatus status = null;
    private String message = null;
    private String consentId = null;
    private ZonedDateTime created = null;
    private EnergyDirection energyDirection = null;
    private Integer participationFactor = null;

    public EdaPermissionRequestBuilder setConnectionId(String connectionId) {
        this.connectionId = connectionId;
        return this;
    }

    public EdaPermissionRequestBuilder setPermissionId(String permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public EdaPermissionRequestBuilder setDataNeedId(String dataNeedId) {
        this.dataNeedId = dataNeedId;
        return this;
    }

    public EdaPermissionRequestBuilder setCmRequestId(String cmRequestId) {
        this.cmRequestId = cmRequestId;
        return this;
    }

    public EdaPermissionRequestBuilder setConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public EdaPermissionRequestBuilder setMeteringPointId(String meteringPointId) {
        this.meteringPointId = meteringPointId;
        return this;
    }

    public EdaPermissionRequestBuilder setDsoId(String dsoId) {
        this.dsoId = dsoId;
        return this;
    }

    public EdaPermissionRequestBuilder setStart(LocalDate start) {
        this.start = start;
        return this;
    }

    public EdaPermissionRequestBuilder setEnd(LocalDate end) {
        this.end = end;
        return this;
    }

    public EdaPermissionRequestBuilder setGranularity(AllowedGranularity granularity) {
        this.granularity = granularity;
        return this;
    }

    public EdaPermissionRequestBuilder setStatus(PermissionProcessStatus status) {
        this.status = status;
        return this;
    }

    public EdaPermissionRequestBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public EdaPermissionRequestBuilder setConsentId(String consentId) {
        this.consentId = consentId;
        return this;
    }

    public EdaPermissionRequestBuilder setCreated(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public EdaPermissionRequestBuilder setEnergyDirection(EnergyDirection energyDirection) {
        this.energyDirection = energyDirection;
        return this;
    }

    public EdaPermissionRequestBuilder setParticipationFactor(Integer participationFactor) {
        this.participationFactor = participationFactor;
        return this;
    }

    public EdaPermissionRequest build() {
        return new EdaPermissionRequest(connectionId,
                                        permissionId,
                                        dataNeedId,
                                        cmRequestId,
                                        conversationId,
                                        meteringPointId,
                                        dsoId,
                                        start,
                                        end,
                                        granularity,
                                        status,
                                        message,
                                        consentId,
                                        created,
                                        energyDirection,
                                        participationFactor);
    }
}