// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.nl.mijn.aansluiting.permission;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.nl.mijn.aansluiting.MijnAansluitingRegionConnectorMetadata;
import jakarta.persistence.Embeddable;

@Embeddable
public class MijnAansluitingDataSourceInformation extends DataSourceInformation {
    private static final MijnAansluitingRegionConnectorMetadata regionConnectorMetadata = MijnAansluitingRegionConnectorMetadata.getInstance();

    private static final String MIJN_AANSLUITING = "Mijn Aansluiting";

    public MijnAansluitingDataSourceInformation() {
        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.meteredDataAdministratorId = MIJN_AANSLUITING;
        this.permissionAdministratorId = MIJN_AANSLUITING;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MijnAansluitingDataSourceInformation;
    }
}
