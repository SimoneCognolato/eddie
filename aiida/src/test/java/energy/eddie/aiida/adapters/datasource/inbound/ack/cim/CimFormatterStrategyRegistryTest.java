// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.aiida.adapters.datasource.inbound.ack.cim;

import energy.eddie.aiida.errors.formatter.CimSchemaFormatterException;
import energy.eddie.api.agnostic.aiida.AiidaSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CimFormatterStrategyRegistryTest {
    private final CimFormatterStrategyRegistry registry = new CimFormatterStrategyRegistry();

    @Test
    void strategyFor_returnsMinMaxEnvelopeCimFormatterStrategyForMinMaxEnvelopeCimV1_12() throws Exception {
        // When
        var strategy = registry.strategyFor(AiidaSchema.MIN_MAX_ENVELOPE_CIM_V1_12);

        // Then
        assertInstanceOf(MinMaxEnvelopeCimFormatterStrategy.class, strategy);
    }

    @Test
    void strategyFor_throwsCimSchemaFormatterExceptionForUnsupportedSchema() {
        // When, Then
        assertThrows(CimSchemaFormatterException.class,
                     () -> registry.strategyFor(AiidaSchema.SMART_METER_P1_CIM_V1_04));
    }
}
