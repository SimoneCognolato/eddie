// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.cds.services.oauth.authorization;

import energy.eddie.cim.agnostic.PermissionProcessStatus;

public record UnauthorizedResult(String permissionId,
                                 PermissionProcessStatus status) implements AuthorizationResult {
}
