// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.cim.agnostic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SimpleDataSourceInformation(String countryCode,
                                          String regionConnectorId,
                                          String meteredDataAdministratorId,
                                          String permissionAdministratorId) implements DataSourceInformation {}
