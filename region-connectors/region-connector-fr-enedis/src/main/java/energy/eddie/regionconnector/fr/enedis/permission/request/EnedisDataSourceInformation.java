// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.fr.enedis.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.fr.enedis.EnedisRegionConnectorMetadata;

public class EnedisDataSourceInformation extends DataSourceInformation {
    private static final EnedisRegionConnectorMetadata regionConnectorMetadata = EnedisRegionConnectorMetadata.getInstance();

    private static final String ENEDIS = "Enedis";

    public EnedisDataSourceInformation() {
        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.permissionAdministratorId = ENEDIS;
        this.meteredDataAdministratorId = ENEDIS;
    }
}
