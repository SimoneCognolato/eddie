// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.de.eta;

import energy.eddie.cim.agnostic.DataSourceInformation;

/**
 * Data source information for the German (DE) ETA Plus region connector.
 * This class provides information about the permission administrator and
 * metered data administrator for Germany.
 */
public class DeDataSourceInformation extends DataSourceInformation {

    private static final String PERMISSION_ADMINISTRATOR_ID = "eta-plus";
    private static final String METERED_DATA_ADMINISTRATOR_ID = "eta-plus";

    public DeDataSourceInformation() {
        this.countryCode = EtaRegionConnectorMetadata.COUNTRY_CODE;
        this.regionConnectorId = EtaRegionConnectorMetadata.REGION_CONNECTOR_ID;
        this.meteredDataAdministratorId = METERED_DATA_ADMINISTRATOR_ID;
        this.permissionAdministratorId = PERMISSION_ADMINISTRATOR_ID;
    }
}
