// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.outbound.rest.tasks;

import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.outbound.rest.model.ConnectionStatusMessageModel;
import energy.eddie.outbound.rest.persistence.ConnectionStatusMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsertionTaskTest {
    @Mock
    private ConnectionStatusMessageRepository repository;

    @Test
    void insertsConnectionStatusMessages() {
        // Given
        var csm = new ConnectionStatusMessage("cid", "pid", "dnid", null, PermissionProcessStatus.CREATED);

        // When
        new InsertionTask<>(Flux.just(csm), repository, ConnectionStatusMessageModel::new);

        // Then
        verify(repository).save(new ConnectionStatusMessageModel(csm));
    }
}