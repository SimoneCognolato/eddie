// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.shared.permission.requests;

import energy.eddie.cim.agnostic.DataSourceInformation;

public class DummyDataSourceInformation extends DataSourceInformation {
    public DummyDataSourceInformation() {
        this.countryCode = "AT";
        this.regionConnectorId = "dummy-rc";
    }
}
