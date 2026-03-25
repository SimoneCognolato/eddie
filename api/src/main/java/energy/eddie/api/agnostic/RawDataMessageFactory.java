// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.api.agnostic;

import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.cim.agnostic.RawDataMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Factory for creating {@link RawDataMessage} instances
 */
public final class RawDataMessageFactory {

    private RawDataMessageFactory() {
        // utility class - not instantiable
    }

    /**
     * Creates a {@link RawDataMessage} from the given permission request and raw payload.
     * <p>
     * All message fields except {@code timestamp} and {@code rawPayload} are sourced from the permission request.
     * The timestamp is always set to the current UTC time at the moment of the call.
     *
     * @param permissionRequest provides the permission, connection, data need, and data source metadata
     * @param rawPayload        the raw data payload to include in the message
     * @return a new {@link RawDataMessage} populated from the given arguments
     */
    public static RawDataMessage create(PermissionRequest permissionRequest, String rawPayload) {
        return new RawDataMessage(
                permissionRequest.permissionId(),
                permissionRequest.connectionId(),
                permissionRequest.dataNeedId(),
                permissionRequest.dataSourceInformation(),
                ZonedDateTime.now(ZoneOffset.UTC),
                rawPayload
        );
    }
}
