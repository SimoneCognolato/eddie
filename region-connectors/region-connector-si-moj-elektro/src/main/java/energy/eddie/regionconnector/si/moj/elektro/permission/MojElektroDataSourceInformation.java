// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.si.moj.elektro.permission;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.si.moj.elektro.MojElektroRegionConnectorMetadata;

public class MojElektroDataSourceInformation extends DataSourceInformation {

    private static final String MOJ_ELEKTRO = "Moj Elektro";

    public MojElektroDataSourceInformation() {
        this.countryCode = MojElektroRegionConnectorMetadata.COUNTRY_CODE;
        this.regionConnectorId = MojElektroRegionConnectorMetadata.REGION_CONNECTOR_ID;
        this.meteredDataAdministratorId = MOJ_ELEKTRO;
        this.permissionAdministratorId = MOJ_ELEKTRO;
    }
}
