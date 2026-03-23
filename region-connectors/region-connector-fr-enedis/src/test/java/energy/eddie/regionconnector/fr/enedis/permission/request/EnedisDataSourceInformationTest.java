// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.fr.enedis.permission.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnedisDataSourceInformationTest {

    @Test
    void countryCode() {
        var enedisdataSourceInformation = new EnedisDataSourceInformation();

        var countryCode = enedisdataSourceInformation.getCountryCode();

        assertEquals("FR", countryCode);
    }

    @Test
    void permissionAdministratorId() {
        var enedisdataSourceInformation = new EnedisDataSourceInformation();

        var permissionAdministratorId = enedisdataSourceInformation.getPermissionAdministratorId();

        assertEquals("Enedis", permissionAdministratorId);
    }

    @Test
    void meteredDataAdministratorId() {
        var enedisdataSourceInformation = new EnedisDataSourceInformation();

        var meteredDataAdministratorId = enedisdataSourceInformation.getMeteredDataAdministratorId();

        assertEquals("Enedis", meteredDataAdministratorId);
    }

    @Test
    void regionConnectorId() {
        var enedisdataSourceInformation = new EnedisDataSourceInformation();

        var regionConnectorId = enedisdataSourceInformation.getRegionConnectorId();

        assertEquals("fr-enedis", regionConnectorId);
    }
}