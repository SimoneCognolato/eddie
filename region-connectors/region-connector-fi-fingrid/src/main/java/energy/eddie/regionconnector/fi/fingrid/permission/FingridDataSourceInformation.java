// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.fi.fingrid.permission;

import energy.eddie.api.v0.RegionConnectorMetadata;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.fi.fingrid.FingridRegionConnectorMetadata;

public class FingridDataSourceInformation extends DataSourceInformation {

    private static final RegionConnectorMetadata REGION_CONNECTOR_METADATA = FingridRegionConnectorMetadata.INSTANCE;

    public FingridDataSourceInformation() {
        this.countryCode = REGION_CONNECTOR_METADATA.countryCode();
        this.regionConnectorId = REGION_CONNECTOR_METADATA.id();
        this.meteredDataAdministratorId = "Fingrid";
        this.permissionAdministratorId = "Fingrid";
    }
}
