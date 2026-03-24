// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.simulation;

import energy.eddie.cim.agnostic.DataSourceInformation;

public class SimulationDataSourceInformation implements DataSourceInformation {
    @Override
    public String countryCode() {
        return "DE";
    }

    @Override
    public String regionConnectorId() {
        return "sim";
    }

    @Override
    public String meteredDataAdministratorId() {
        return "sim";
    }

    @Override
    public String permissionAdministratorId() {
        return "sim";
    }
}
