// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.api.agnostic;

import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.cim.agnostic.RawDataMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class RawDataMessageFactory {
    private RawDataMessageFactory() {
        // Private constructor to prevent instantiation
    }

    public static RawDataMessage create(PermissionRequest permissionRequest, String rawPayload) {
        return new RawDataMessage().withPermissionId(permissionRequest.permissionId())
                                   .withConnectionId(permissionRequest.connectionId())
                                   .withDataNeedId(permissionRequest.dataNeedId())
                                   .withDataSourceInformation(permissionRequest.dataSourceInformation())
                                   .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                                   .withRawPayload(rawPayload);
    }
}
