// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.simulation.dtos;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import reactor.util.annotation.Nullable;

public class SetConnectionStatusRequest {
    @Nullable
    public String connectionId;
    @Nullable
    public String dataNeedId;
    @Nullable
    public String permissionId;
    @Nullable
    public PermissionProcessStatus connectionStatus;
}