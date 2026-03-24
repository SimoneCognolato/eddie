// SPDX-FileCopyrightText: 2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.services;

import energy.eddie.api.agnostic.process.model.PermissionRequest;
import energy.eddie.cim.agnostic.PermissionProcessStatus;
import energy.eddie.regionconnector.es.datadis.api.AuthorizationApi;
import energy.eddie.regionconnector.es.datadis.dtos.AuthorizationRequestFactory;
import energy.eddie.regionconnector.es.datadis.permission.events.EsSentToPermissionAdministratorEvent;
import energy.eddie.regionconnector.es.datadis.permission.events.EsSimpleEvent;
import energy.eddie.regionconnector.es.datadis.persistence.EsPermissionRequestRepository;
import energy.eddie.regionconnector.shared.event.sourcing.Outbox;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BundleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BundleService.class);
    private final EsPermissionRequestRepository repository;
    private final AuthorizationRequestFactory authorizationRequestFactory;
    private final AuthorizationApi authorizationApi;
    private final Outbox outbox;
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("NullAway") // EntityManager is injected by spring boot
    @Autowired
    public BundleService(
            EsPermissionRequestRepository repository,
            AuthorizationRequestFactory authorizationRequestFactory,
            AuthorizationApi authorizationApi,
            Outbox outbox
    ) {
        this.repository = repository;
        this.authorizationRequestFactory = authorizationRequestFactory;
        this.authorizationApi = authorizationApi;
        this.outbox = outbox;
    }

    // For testing only
    BundleService(
            EsPermissionRequestRepository repository,
            AuthorizationRequestFactory authorizationRequestFactory,
            AuthorizationApi authorizationApi,
            Outbox outbox,
            EntityManager em
    ) {
        this.repository = repository;
        this.authorizationRequestFactory = authorizationRequestFactory;
        this.authorizationApi = authorizationApi;
        this.outbox = outbox;
        this.em = em;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendBundledAuthorizationRequest(UUID bundleId) {
        LOGGER.debug("Send bundled authorization request for bundle with ID {}", bundleId);
        em.flush();
        em.clear();
        var all = repository.findAllByBundleId(bundleId);
        if (all.isEmpty()) {
            LOGGER.warn("Bundle {} not found", bundleId);
            return;
        }
        var latest = all.getFirst();
        for (var current : all) {
            if (current.end().isAfter(latest.end())) {
                latest = current;
            }
        }

        var authorizationRequest = authorizationRequestFactory.from(
                latest.nif(),
                latest.meteringPointId(),
                latest.end()
        );
        authorizationApi.postAuthorizationRequest(authorizationRequest)
                        .subscribe(res -> {
                            for (var pr : all) {
                                var permissionId = pr.permissionId();
                                LOGGER.info("Sent authorization request for permission request {}", permissionId);
                                outbox.commit(new EsSentToPermissionAdministratorEvent(permissionId,
                                                                                       res.originalResponse()));
                            }
                        }, e -> {
                            LOGGER.atWarn()
                                  .addArgument(bundleId)
                                  .addArgument(() -> all.stream().map(PermissionRequest::permissionId).toList())
                                  .log("Could not send permission request bundle {} containing {} to Datadis", e);
                            for (var pr : all) {
                                outbox.commit(new EsSimpleEvent(pr.permissionId(),
                                                                PermissionProcessStatus.UNABLE_TO_SEND));
                            }
                        });
    }
}
