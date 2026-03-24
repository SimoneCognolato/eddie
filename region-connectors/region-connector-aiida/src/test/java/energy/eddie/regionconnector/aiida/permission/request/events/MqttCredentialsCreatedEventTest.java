// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.aiida.permission.request.events;

import org.junit.jupiter.api.Test;

import static energy.eddie.cim.agnostic.PermissionProcessStatus.ACCEPTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MqttCredentialsCreatedEventTest {
    @Test
    void newMqttCredentialsCreatedEvent_usesStatusAccepted() {
        // When
        MqttCredentialsCreatedEvent event = new MqttCredentialsCreatedEvent("foo");

        // Then
        assertEquals(ACCEPTED, event.status());
        assertEquals("foo", event.permissionId());
    }
}
