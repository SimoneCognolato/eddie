// SPDX-FileCopyrightText: 2023-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request;

import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.regionconnector.at.eda.EdaRegionConnectorMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
@SuppressWarnings("NullAway")
public class EdaDataSourceInformation extends DataSourceInformation {
    private static final EdaRegionConnectorMetadata regionConnectorMetadata = EdaRegionConnectorMetadata.getInstance();
    @Column(length = 8)
    private final String dsoId;

    public EdaDataSourceInformation(String dsoId) {
        this.dsoId = dsoId;
        this.countryCode = regionConnectorMetadata.countryCode();
        this.regionConnectorId = regionConnectorMetadata.id();
        this.meteredDataAdministratorId = dsoId;
        this.permissionAdministratorId = dsoId;
    }

    protected EdaDataSourceInformation() {
        this(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdaDataSourceInformation that)) return false;
        return Objects.equals(dsoId, that.dsoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dsoId);
    }
}
