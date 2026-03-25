// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.at.eda.permission.request.events;

import energy.eddie.cim.agnostic.PermissionProcessStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@SuppressWarnings("NullAway") // Needed for JPA
public class EdaAnswerEvent extends PersistablePermissionEvent {
    @Column(columnDefinition = "text")
    private final String message;

    public EdaAnswerEvent(String permissionId, PermissionProcessStatus status, String message) {
        super(permissionId, status);
        this.message = message;
    }

    protected EdaAnswerEvent() {
        super();
        message = null;
    }

    public String message() {
        return message;
    }
}
