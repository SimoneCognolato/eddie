// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.be.fluvius.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.be.fluvius.FluviusRegionConnectorMetadata;

public class FluviusDataSourceInformation extends DataSourceInformation {
    public static final String FLUVIUS = "Fluvius";

    public FluviusDataSourceInformation() {
        this.countryCode = "BE";
        this.regionConnectorId = FluviusRegionConnectorMetadata.REGION_CONNECTOR_ID;
        this.meteredDataAdministratorId = FLUVIUS;
        this.permissionAdministratorId = FLUVIUS;
    }
}
