// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.core;

import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.cim.agnostic.SimpleDataSourceInformation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperConfigTest {

    @Test
    void canDeserializeDataSourceInformation() {
        // Given
        DataSourceInformation dataSourceInformation = new SimpleDataSourceInformation(
                "at",
                "at-eda",
                "eda",
                "eda"
        );
        var msg = new ConnectionStatusMessage(
                "cid",
                "pid",
                "dnid",
                dataSourceInformation,
                ZonedDateTime.now(ZoneOffset.UTC),
                PermissionProcessStatus.CREATED,
                "",
                null
        );
        var builder = JsonMapper.builder();
        new MapperConfig().jsonCustomizer().customize(builder);
        var mapper = builder.build();
        var str = mapper.writeValueAsString(msg);

        // When
        var res = mapper.readValue(str, ConnectionStatusMessage.class);

        // Then
        assertEquals(msg.dataSourceInformation(), res.dataSourceInformation());
    }
}