// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0
package energy.eddie.api.agnostic;

import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.RawDataMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RawDataMessageFactoryTest {
    private static final String PERMISSION_ID = "perm-123";
    private static final String CONNECTION_ID = "conn-456";
    private static final String DATA_NEED_ID = "need-789";
    private static final DataSourceInformation DATA_SOURCE_INFO = new DataSourceInformation()
            .withCountryCode("AT")
            .withRegionConnectorId("aiida")
            .withPermissionAdministratorId("paid")
            .withMeteredDataAdministratorId("mdid");
    private static final String RAW_PAYLOAD = "{\"value\": 42}";

    @Mock
    private PermissionRequest permissionRequest;

    @BeforeEach
    void setUp() {
        when(permissionRequest.permissionId()).thenReturn(PERMISSION_ID);
        when(permissionRequest.connectionId()).thenReturn(CONNECTION_ID);
        when(permissionRequest.dataNeedId()).thenReturn(DATA_NEED_ID);
        when(permissionRequest.dataSourceInformation()).thenReturn(DATA_SOURCE_INFO);
    }

    @Test
    void create_returnsRawDataMessageWithCorrectFields() {
        // When
        RawDataMessage result = RawDataMessageFactory.create(permissionRequest, RAW_PAYLOAD);

        // Then
        assertAll(
                () -> assertThat(result.getTimestamp()).isCloseTo(ZonedDateTime.now(), within(5, ChronoUnit.SECONDS)),
                () -> assertEquals(PERMISSION_ID, result.getPermissionId()),
                () -> assertEquals(CONNECTION_ID, result.getConnectionId()),
                () -> assertEquals(DATA_NEED_ID, result.getDataNeedId()),
                () -> assertEquals(DATA_SOURCE_INFO, result.getDataSourceInformation()),
                () -> assertEquals(RAW_PAYLOAD, result.getRawPayload())
        );
    }
}