// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.api.agnostic.data.needs;

import energy.eddie.api.agnostic.Granularity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record CESUJoinRequestDataNeedResult(
        LocalDate start,
        List<Granularity> supportedGranularities,
        Optional<EnergyDirection> energyDirection,
        Optional<Integer> participationFactor
) implements DataNeedCalculationResult {
    public CESUJoinRequestDataNeedResult(LocalDate start, List<Granularity> supportedGranularities) {
        this(start, supportedGranularities, Optional.empty(), Optional.empty());
    }
}
