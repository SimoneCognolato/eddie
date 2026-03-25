// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.outbound.shared.testing;

import energy.eddie.cim.agnostic.DataSourceInformation;

public record MockDataSourceInformation(String countryCode,
                                        String regionConnectorId,
                                        String permissionAdministratorId,
                                        String meteredDataAdministratorId) implements DataSourceInformation {
}
