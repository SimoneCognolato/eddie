// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.de.eta;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeDataSourceInformationTest {

    @Test
    void testDataSourceInformationValues() {
        DeDataSourceInformation dsi = new DeDataSourceInformation();
        assertThat(dsi.getCountryCode()).isEqualTo("DE");
        assertThat(dsi.getRegionConnectorId()).isEqualTo("de-eta");
        assertThat(dsi.getPermissionAdministratorId()).isEqualTo("eta-plus");
        assertThat(dsi.getMeteredDataAdministratorId()).isEqualTo("eta-plus");
    }
}
