// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.aiida.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.aiida.AiidaRegionConnectorMetadata;

public class AiidaDataSourceInformation extends DataSourceInformation {
    public AiidaDataSourceInformation() {
        var regionConnectorMetadata = AiidaRegionConnectorMetadata.getInstance();

        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.meteredDataAdministratorId = regionConnectorMetadata.id();
        this.permissionAdministratorId = regionConnectorMetadata.id();
    }
}
