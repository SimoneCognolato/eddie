// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.aiida.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.aiida.AiidaRegionConnectorMetadata;

public class AiidaDataSourceInformation implements DataSourceInformation {
    private static final AiidaRegionConnectorMetadata regionConnectorMetadata = AiidaRegionConnectorMetadata.getInstance();

    @Override
    public String countryCode() {
        return regionConnectorMetadata.countryCode();
    }

    @Override
    public String regionConnectorId() {
        return regionConnectorMetadata.id();
    }

    @Override
    public String meteredDataAdministratorId() {
        return regionConnectorMetadata.id();
    }

    @Override
    public String permissionAdministratorId() {
        return regionConnectorMetadata.id();
    }
}
