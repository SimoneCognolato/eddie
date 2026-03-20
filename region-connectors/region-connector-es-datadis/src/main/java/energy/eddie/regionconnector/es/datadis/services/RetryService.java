// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis.services;

import energy.eddie.api.v0.PermissionProcessStatus;
import energy.eddie.regionconnector.es.datadis.permission.events.EsValidatedEvent;
import energy.eddie.regionconnector.es.datadis.permission.request.api.EsPermissionRequest;
import energy.eddie.regionconnector.es.datadis.persistence.EsPermissionRequestRepository;
import energy.eddie.regionconnector.shared.event.sourcing.Outbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RetryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryService.class);
    private final Outbox outbox;
    private final EsPermissionRequestRepository repository;
    private final BundleService bundleService;

    public RetryService(Outbox outbox, EsPermissionRequestRepository repository, BundleService bundleService) {
        this.outbox = outbox;
        this.repository = repository;
        this.bundleService = bundleService;
    }

    @SuppressWarnings("java:S6857") // Sonar and cron syntax don't play together
    @Scheduled(cron = "${region-connector.es.datadis.retry:0 0 * * * *}")
    public void retry() {
        LOGGER.info("Retrying failed permission requests");
        List<EsPermissionRequest> permissionRequests = repository.findByStatus(PermissionProcessStatus.UNABLE_TO_SEND);
        Set<UUID> bundles = new HashSet<>();
        for (EsPermissionRequest pr : permissionRequests) {
            var permissionId = pr.permissionId();
            if (pr.bundleId() != null) {
                LOGGER.debug("Bundling permission request {} for bundle {}", permissionId, pr.bundleId());
                bundles.add(pr.bundleId());
            }
            LOGGER.info("Retrying permission request {}", permissionId);
            outbox.commit(new EsValidatedEvent(permissionId,
                                               pr.start(),
                                               pr.end(),
                                               pr.allowedGranularity(),
                                               pr.bundleId()));
        }
        for (var bundle : bundles) {
            LOGGER.debug("Retrying all permission requests in bundle {}", bundle);
            bundleService.sendBundledAuthorizationRequest(bundle);
        }
    }
}
