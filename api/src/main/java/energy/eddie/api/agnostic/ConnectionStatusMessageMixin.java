// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.api.agnostic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import energy.eddie.cim.agnostic.DataSourceInformation;

public abstract class ConnectionStatusMessageMixin {
    // Without type information embedded in JSON, explicitly specify the deserialization class for DataSourceInformation
    // @JsonDeserialize(as = DataSourceInformationImpl.class)
    // Or just ignore the field if it is not needed
    @JsonIgnore
    public abstract DataSourceInformation dataSourceInformation();
}
