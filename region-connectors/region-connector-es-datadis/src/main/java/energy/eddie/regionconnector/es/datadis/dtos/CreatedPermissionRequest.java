// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.dtos;

import java.util.List;

public record CreatedPermissionRequest(List<String> permissionIds) {
    public CreatedPermissionRequest(String permissionId) {
        this(List.of(permissionId));
    }
}
