// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.dk.energinet.permission.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnerginetDataSourceInformationTest {

    @Test
    void countryCode() {
        var energinetdataSourceInformation = new EnerginetDataSourceInformation();

        var countryCode = energinetdataSourceInformation.getCountryCode();

        assertEquals("DK", countryCode);
    }

    @Test
    void regionConnectorId() {
        var energinetdataSourceInformation = new EnerginetDataSourceInformation();

        var regionConnectorId = energinetdataSourceInformation.getRegionConnectorId();

        assertEquals("dk-energinet", regionConnectorId);
    }

    @Test
    void permissionAdministratorId() {
        var energinetdataSourceInformation = new EnerginetDataSourceInformation();

        var permissionAdministratorId = energinetdataSourceInformation.getPermissionAdministratorId();

        assertEquals("Energinet", permissionAdministratorId);
    }

    @Test
    void meteredDataAdministratorId() {
        var energinetdataSourceInformation = new EnerginetDataSourceInformation();

        var meteredDataAdministratorId = energinetdataSourceInformation.getMeteredDataAdministratorId();

        assertEquals("Energinet", meteredDataAdministratorId);
    }
}