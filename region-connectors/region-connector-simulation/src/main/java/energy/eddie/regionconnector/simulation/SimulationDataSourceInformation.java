// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.simulation;

import energy.eddie.cim.agnostic.DataSourceInformation;

public class SimulationDataSourceInformation extends DataSourceInformation {
    public SimulationDataSourceInformation() {
        this.countryCode = "DE";
        this.regionConnectorId = "sim";
        this.meteredDataAdministratorId = "sim";
        this.permissionAdministratorId = "sim";
    }
}
