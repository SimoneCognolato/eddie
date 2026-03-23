// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.us.green.button.permission;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.us.green.button.GreenButtonRegionConnectorMetadata;
import jakarta.persistence.Embeddable;

@Embeddable
@SuppressWarnings("NullAway")
public class GreenButtonDataSourceInformation extends DataSourceInformation {
    private static final GreenButtonRegionConnectorMetadata regionConnectorMetadata = GreenButtonRegionConnectorMetadata.getInstance();

    public GreenButtonDataSourceInformation(String dsoId, String countryCode) {
        this.countryCode = countryCode;
        this.regionConnectorId = regionConnectorMetadata.id();
        this.permissionAdministratorId = dsoId;
        this.meteredDataAdministratorId = dsoId;
    }

    protected GreenButtonDataSourceInformation() {
        this(null, null);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof GreenButtonDataSourceInformation;
    }
}
