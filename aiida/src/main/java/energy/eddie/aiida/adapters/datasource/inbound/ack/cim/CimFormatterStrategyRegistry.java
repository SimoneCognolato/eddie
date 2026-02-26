// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.aiida.adapters.datasource.inbound.ack.cim;

import energy.eddie.aiida.errors.formatter.CimSchemaFormatterException;
import energy.eddie.api.agnostic.aiida.AiidaSchema;

public class CimFormatterStrategyRegistry {
    public CimFormatterStrategy strategyFor(AiidaSchema schema) throws CimSchemaFormatterException {
        if (schema == AiidaSchema.MIN_MAX_ENVELOPE_CIM_V1_12) {
            return new MinMaxEnvelopeCimFormatterStrategy();
        }

        throw new CimSchemaFormatterException(
                new IllegalArgumentException("No CIM formatter strategy found for schema " + schema));
    }
}
