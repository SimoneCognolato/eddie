// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.es.datadis.DatadisRegionConnectorMetadata;
import energy.eddie.regionconnector.es.datadis.permission.request.api.EsPermissionRequest;

public class DatadisDataSourceInformation extends DataSourceInformation {
    private static final DatadisRegionConnectorMetadata regionConnectorMetadata = DatadisRegionConnectorMetadata.getInstance();
    private final EsPermissionRequest permissionRequest;

    public DatadisDataSourceInformation(EsPermissionRequest permissionRequest) {
        this.permissionRequest = permissionRequest;

        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.meteredDataAdministratorId = meteredDataAdministratorId();
        this.permissionAdministratorId = "Datadis";
    }

    private String meteredDataAdministratorId() {
        return permissionRequest.distributorCode()
                                .map(DistributorCode::toString)
                                .orElse("Not available");
    }
}
