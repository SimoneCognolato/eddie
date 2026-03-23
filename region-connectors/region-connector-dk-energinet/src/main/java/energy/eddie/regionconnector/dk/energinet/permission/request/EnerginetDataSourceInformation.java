// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.dk.energinet.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.dk.energinet.EnerginetRegionConnectorMetadata;

public class EnerginetDataSourceInformation extends DataSourceInformation {
    private static final EnerginetRegionConnectorMetadata regionConnectorMetadata = EnerginetRegionConnectorMetadata.getInstance();

    private static final String ENERGINET = "Energinet";

    public EnerginetDataSourceInformation() {
        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.meteredDataAdministratorId = ENERGINET;
        this.permissionAdministratorId = ENERGINET;
    }
}
