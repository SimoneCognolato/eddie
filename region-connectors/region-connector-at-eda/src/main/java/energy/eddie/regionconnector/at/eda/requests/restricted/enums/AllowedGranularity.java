// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.requests.restricted.enums;

import energy.eddie.api.agnostic.Granularity;

public enum AllowedGranularity {
    PT15M,
    P1D;

    public static AllowedGranularity valueOf(Granularity granularity) {
        return switch (granularity) {
            case PT15M -> AllowedGranularity.PT15M;
            case P1D -> AllowedGranularity.P1D;
            default -> throw new IllegalArgumentException("Unsupported granularity: " + granularity);
        };
    }
}
