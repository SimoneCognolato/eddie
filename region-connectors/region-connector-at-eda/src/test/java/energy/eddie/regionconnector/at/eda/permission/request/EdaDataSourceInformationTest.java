// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EdaDataSourceInformationTest {

    @Test
    void countryCode() {
        EdaDataSourceInformation edadataSourceInformation = new EdaDataSourceInformation("dsoId");
        assertEquals("AT", edadataSourceInformation.getCountryCode());
    }

    @Test
    void regionConnectorId() {
        EdaDataSourceInformation edadataSourceInformation = new EdaDataSourceInformation("dsoId");
        assertEquals("at-eda", edadataSourceInformation.getRegionConnectorId());
    }

    @Test
    void permissionAdministratorId_returnsDsoId() {
        String expected = "dsoId";
        EdaDataSourceInformation edadataSourceInformation = new EdaDataSourceInformation(expected);

        assertEquals(expected, edadataSourceInformation.getPermissionAdministratorId());
    }

    @Test
    void meteredDataAdministratorId_returnsDsoId() {
        String expected = "dsoId";
        EdaDataSourceInformation edadataSourceInformation = new EdaDataSourceInformation(expected);

        assertEquals(expected, edadataSourceInformation.getMeteredDataAdministratorId());
    }
}