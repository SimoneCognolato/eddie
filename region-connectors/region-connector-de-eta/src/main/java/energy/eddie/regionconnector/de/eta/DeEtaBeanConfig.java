package energy.eddie.regionconnector.de.eta;

import energy.eddie.api.agnostic.data.needs.DataNeedCalculationService;
import energy.eddie.api.cim.config.CommonInformationModelConfiguration;
import energy.eddie.api.v0.RegionConnectorMetadata;
import energy.eddie.dataneeds.needs.DataNeed;
import energy.eddie.dataneeds.rules.DataNeedRuleSet;
import energy.eddie.dataneeds.services.DataNeedsService;
import energy.eddie.regionconnector.de.eta.config.DeEtaPlusConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import energy.eddie.regionconnector.de.eta.permission.request.DePermissionRequest;
import energy.eddie.regionconnector.de.eta.persistence.DePermissionRequestRepository;
import energy.eddie.regionconnector.de.eta.persistence.DePermissionEventRepository;
import energy.eddie.regionconnector.shared.cim.v0_82.TransmissionScheduleProvider;
import energy.eddie.regionconnector.shared.event.sourcing.EventBus;
import energy.eddie.regionconnector.shared.event.sourcing.EventBusImpl;
import energy.eddie.regionconnector.shared.event.sourcing.Outbox;
import energy.eddie.regionconnector.shared.event.sourcing.handlers.integration.ConnectionStatusMessageHandler;
import energy.eddie.regionconnector.shared.event.sourcing.handlers.integration.PermissionMarketDocumentMessageHandler;
import energy.eddie.regionconnector.shared.services.data.needs.DataNeedCalculationServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneOffset;

/**
 * Spring configuration for the German (DE) ETA Plus region connector.
 * This configuration class sets up beans and dependencies required by the
 * region connector.
 */
@Configuration
@EnableConfigurationProperties(DeEtaPlusConfiguration.class)
public class DeEtaBeanConfig {

    @Bean
    public EventBus eventBus() {
        return new EventBusImpl();
    }

    @Bean
    public Outbox deEtaOutbox(EventBus eventBus, DePermissionEventRepository eventRepository) {
        return new Outbox(eventBus, eventRepository);
    }

    // For connection status messages
    @Bean("deConnectionStatusMessageHandler")
    public ConnectionStatusMessageHandler<DePermissionRequest> connectionStatusMessageHandler(
            EventBus eventBus,
            DePermissionRequestRepository repository
    ) {
        return new ConnectionStatusMessageHandler<>(
                eventBus,
                repository,
                pr -> ""
        );
    }

    // For permission market documents, the CIM pendant to connection status
    // messages
    @Bean("dePermissionMarketDocumentMessageHandler")
    public PermissionMarketDocumentMessageHandler<DePermissionRequest> permissionMarketDocumentMessageHandler(
            EventBus eventBus,
            DePermissionRequestRepository repository,
            DataNeedsService dataNeedsService,
            CommonInformationModelConfiguration cimConfig
    ) {
        return new PermissionMarketDocumentMessageHandler<>(
                eventBus,
                repository,
                dataNeedsService,
                cimConfig.eligiblePartyFallbackId(),
                cimConfig,
                pr -> null,
                ZoneOffset.UTC
        );
    }

    @Bean
    public TransmissionScheduleProvider<DePermissionRequest> deTransmissionScheduleProvider() {
        return permissionRequest -> null; // Return null for no specific transmission schedule
    }

    @Bean
    public DataNeedCalculationService<DataNeed> dataNeedCalculationService(
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DataNeedsService dataNeedsService,
            RegionConnectorMetadata metadata,
            DataNeedRuleSet ruleSet
    ) {
        return new DataNeedCalculationServiceImpl(dataNeedsService, metadata, ruleSet);
    }
}
