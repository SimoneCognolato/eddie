// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.cds.providers;

import energy.eddie.api.v0.PermissionProcessStatus;
import energy.eddie.cim.agnostic.KeyValuePair;
import energy.eddie.regionconnector.cds.permission.requests.CdsPermissionRequest;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.function.Function;

class RedirectUriExtension implements Function<CdsPermissionRequest, List<KeyValuePair>> {
    @Override
    @Nullable
    public List<KeyValuePair> apply(CdsPermissionRequest permissionRequest) {
        var redirectUri = permissionRequest.redirectUri();
        if (permissionRequest.status() != PermissionProcessStatus.SENT_TO_PERMISSION_ADMINISTRATOR || redirectUri.isEmpty()) {
            return null;
        }
        return List.of(
                new KeyValuePair().withKey("redirectUri")
                                  .withValue(permissionRequest.redirectUri().orElse(null)));
    }
}
