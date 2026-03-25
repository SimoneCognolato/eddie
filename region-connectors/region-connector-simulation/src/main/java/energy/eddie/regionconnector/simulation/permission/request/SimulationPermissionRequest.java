// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.simulation.permission.request;

import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.regionconnector.simulation.SimulationDataSourceInformation;
import energy.eddie.regionconnector.simulation.dtos.SetConnectionStatusRequest;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public record SimulationPermissionRequest(@Nullable String connectionId, @Nullable String permissionId,
                                          @Nullable String dataNeedId,
                                          @Nullable PermissionProcessStatus status) implements PermissionRequest {

    public SimulationPermissionRequest(SetConnectionStatusRequest req) {
        this(req.connectionId, req.permissionId, req.dataNeedId, req.connectionStatus);
    }

    @Override
    public DataSourceInformation dataSourceInformation() {
        return new SimulationDataSourceInformation();
    }

    @Override
    public ZonedDateTime created() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public LocalDate start() {
        return LocalDate.of(2021, 1, 1);
    }

    @Override
    public LocalDate end() {
        return LocalDate.of(9999, 12, 31);
    }
}
