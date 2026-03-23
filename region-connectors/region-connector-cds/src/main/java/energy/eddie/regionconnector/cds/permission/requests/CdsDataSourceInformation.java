// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.cds.permission.requests;

import energy.eddie.cim.agnostic.DataSourceInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static energy.eddie.regionconnector.cds.CdsRegionConnectorMetadata.REGION_CONNECTOR_ID;

@Embeddable
public class CdsDataSourceInformation extends DataSourceInformation {
    @Column(name = "cds_server_id")
    private final long cdsServerId;

    public CdsDataSourceInformation(long cdsServerId) {
        this.cdsServerId = cdsServerId;
        this.countryCode = "US";
        this.regionConnectorId = REGION_CONNECTOR_ID;
        this.meteredDataAdministratorId = Objects.toString(cdsServerId);
        this.permissionAdministratorId = meteredDataAdministratorId;
    }

    @SuppressWarnings("NullAway")
    protected CdsDataSourceInformation() {
        this(0);
    }

    public Long cdsServerId() {
        return cdsServerId;
    }
}
