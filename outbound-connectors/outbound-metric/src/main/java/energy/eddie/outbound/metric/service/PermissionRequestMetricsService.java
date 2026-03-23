// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.outbound.metric.service;

import energy.eddie.api.agnostic.outbound.PermissionEventRepositories;
import energy.eddie.api.agnostic.process.model.events.PermissionEvent;
import energy.eddie.api.agnostic.process.model.events.PermissionEventRepository;
import energy.eddie.api.v0.PermissionProcessStatus;
import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.Status;
import energy.eddie.dataneeds.services.DataNeedsService;
import energy.eddie.outbound.metric.connectors.AgnosticConnector;
import energy.eddie.outbound.metric.model.MeanCountRecord;
import energy.eddie.outbound.metric.model.PermissionRequestMetricsModel;
import energy.eddie.outbound.metric.model.PermissionRequestStatusDurationModel;
import energy.eddie.outbound.metric.repositories.PermissionRequestMetricsRepository;
import energy.eddie.outbound.metric.repositories.PermissionRequestStatusDurationRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionRequestMetricsService {

    private final PermissionRequestMetricsRepository metricsRepository;
    private final PermissionRequestStatusDurationRepository statusDurationRepository;
    private final DataNeedsService dataNeedsService;
    private final PermissionEventRepositories repositories;

    public PermissionRequestMetricsService(AgnosticConnector connector,
                                           PermissionRequestMetricsRepository metricsRepository,
                                           PermissionRequestStatusDurationRepository statusDurationRepository,
                                           @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                           DataNeedsService dataNeedsService,
                                           @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                           PermissionEventRepositories repositories) {
        this.metricsRepository = metricsRepository;
        this.statusDurationRepository = statusDurationRepository;
        this.dataNeedsService = dataNeedsService;
        this.repositories = repositories;
        connector.getConnectionStatusMessageStream().subscribe(this::upsertMetric);
    }

    public void upsertMetric(ConnectionStatusMessage csm) {

        var status = csm.getStatus();
        if (status.equals(Status.CREATED)) {
            return;
        }

        var dataSourceInfo = csm.getDataSourceInformation();

        String regionConnectorId = dataSourceInfo.getRegionConnectorId();
        String permissionId = csm.getPermissionId();
        List<PermissionEvent> permissionEvents = getCurrentAndPreviousPermissionEvents(
                permissionId,
                csm.getTimestamp(),
                regionConnectorId
        );

        if(permissionEvents.size() < 2) {
            return;
        }

        ZonedDateTime currentPermissionEventCreated = permissionEvents.getFirst().eventCreated();
        PermissionEvent prevPermissionEvent = permissionEvents.getLast();
        long durationMilliseconds = Duration.between(prevPermissionEvent.eventCreated(), currentPermissionEventCreated)
                .toMillis();
        PermissionProcessStatus prevEventStatus = prevPermissionEvent.status();
        String dataNeedType = dataNeedsService.getById(csm.getDataNeedId()).type();
        String permissionAdministratorId = dataSourceInfo.getPermissionAdministratorId();
        String countryCode = dataSourceInfo.getCountryCode();
        PermissionRequestStatusDurationModel prStatusDuration =  new PermissionRequestStatusDurationModel(
                permissionId,
                prevEventStatus,
                durationMilliseconds,
                dataNeedType,
                permissionAdministratorId,
                regionConnectorId,
                countryCode
        );
        statusDurationRepository.save(prStatusDuration);
        Optional<PermissionRequestMetricsModel> prMetrics = metricsRepository.getPermissionRequestMetrics(
                prevEventStatus,
                dataNeedType,
                permissionAdministratorId,
                regionConnectorId,
                countryCode
        );

        MeanCountRecord newMeanAndCount = getNewMeanAndCount(prMetrics, durationMilliseconds);
        double median = statusDurationRepository.getMedianDurationMilliseconds(
                prevEventStatus.name(),
                dataNeedType,
                permissionAdministratorId,
                regionConnectorId,
                countryCode
        );

        metricsRepository.upsertPermissionRequestMetric(
                newMeanAndCount.mean(),
                median,
                newMeanAndCount.count(),
                prevEventStatus.name(),
                dataNeedType,
                permissionAdministratorId,
                regionConnectorId,
                countryCode
        );
    }

    private MeanCountRecord getNewMeanAndCount(
            Optional<PermissionRequestMetricsModel> prMetrics,
            long durationMilliseconds
    ) {
        int currentCount = prMetrics.map(PermissionRequestMetricsModel::getPermissionRequestCount).orElse(0);
        int newCount = currentCount + 1;
        double currentMean = prMetrics.map(PermissionRequestMetricsModel::getMean).orElse(0.0);
        double newMean = ((currentMean * currentCount) + durationMilliseconds) / newCount;

        return new MeanCountRecord(newMean, newCount);
    }

    private List<PermissionEvent> getCurrentAndPreviousPermissionEvents(String permissionId,
                                                                       ZonedDateTime eventCreated,
                                                                       String regionConnectorId) {
        Optional<PermissionEventRepository> repository = repositories.getPermissionEventRepositoryByRegionConnectorId(
                regionConnectorId
        );

        if(repository.isPresent()) {
            return repository.get().findTop2ByPermissionIdAndEventCreatedLessThanEqualOrderByEventCreatedDesc(
                    permissionId,
                    eventCreated
            );
        }

        return List.of();
    }
}
