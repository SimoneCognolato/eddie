// SPDX-FileCopyrightText: 2025-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.outbound.rest.persistence;

import energy.eddie.cim.agnostic.ConnectionStatusMessage;
import energy.eddie.cim.agnostic.DataSourceInformation;
import energy.eddie.cim.agnostic.Status;
import energy.eddie.outbound.rest.RestTestConfig;
import energy.eddie.outbound.rest.model.ConnectionStatusMessageModel;
import energy.eddie.outbound.rest.persistence.specifications.InsertionTimeSpecification;
import energy.eddie.outbound.rest.persistence.specifications.JsonPathSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DirtiesContext
@Import({PersistenceConfig.class, RestTestConfig.class})
class ConnectionStatusMessageRepositoryTest {
    @SuppressWarnings("unused")
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15-alpine");
    @Autowired
    private ConnectionStatusMessageRepository connectionStatusMessageRepository;

    @Test
    void specificationForJsonPath_returnsCorrectConnectionStatusMessage() {
        // Given
        var spec = new JsonPathSpecification<ConnectionStatusMessageModel>("permissionId", "pid");
        var dataSourceInformation = new DataSourceInformation().withCountryCode("at")
                                                               .withRegionConnectorId("at-eda")
                                                               .withPermissionAdministratorId("eda")
                                                               .withMeteredDataAdministratorId("eda");
        var payload = new ConnectionStatusMessage().withConnectionId("cid")
                                                   .withPermissionId("pid")
                                                   .withDataNeedId("dnid")
                                                   .withDataSourceInformation(dataSourceInformation)
                                                   .withStatus(Status.ACCEPTED);

        var otherDataSourceInformation = new DataSourceInformation().withCountryCode("at")
                                                                    .withRegionConnectorId("at-eda")
                                                                    .withPermissionAdministratorId("eda")
                                                                    .withMeteredDataAdministratorId("eda");
        var otherPayload = new ConnectionStatusMessage().withConnectionId("cid")
                                                        .withPermissionId("other-pid")
                                                        .withDataNeedId("dnid")
                                                        .withDataSourceInformation(otherDataSourceInformation)
                                                        .withStatus(Status.ACCEPTED);
        var csm = connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(payload));
        connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(otherPayload));

        // When
        var res = connectionStatusMessageRepository.findAll(spec);

        // Then
        assertThat(res)
                .singleElement()
                .isEqualTo(csm);
    }

    @Test
    void specificationForNestedJsonPath_returnsCorrectConnectionStatusMessage() {
        // Given
        var spec = new JsonPathSpecification<ConnectionStatusMessageModel>(List.of("dataSourceInformation",
                                                                                   "countryCode"), "be");

        var dataSourceInformation = new DataSourceInformation().withCountryCode("be")
                                                               .withRegionConnectorId("be-fluvius")
                                                               .withPermissionAdministratorId("fluvius")
                                                               .withMeteredDataAdministratorId("fluvius");
        var payload = new ConnectionStatusMessage().withConnectionId("cid")
                                                   .withPermissionId("pid")
                                                   .withDataNeedId("dnid")
                                                   .withDataSourceInformation(dataSourceInformation)
                                                   .withStatus(Status.ACCEPTED);

        var otherDataSourceInformation = new DataSourceInformation().withCountryCode("at")
                                                                    .withRegionConnectorId("at-eda")
                                                                    .withPermissionAdministratorId("eda")
                                                                    .withMeteredDataAdministratorId("eda");
        var otherPayload = new ConnectionStatusMessage().withConnectionId("cid")
                                                        .withPermissionId("other-pid")
                                                        .withDataNeedId("dnid")
                                                        .withDataSourceInformation(otherDataSourceInformation)
                                                        .withStatus(Status.ACCEPTED);
        var csm = connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(payload));
        connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(otherPayload));

        // When
        var res = connectionStatusMessageRepository.findAll(spec);

        // Then
        assertThat(res)
                .singleElement()
                .isEqualTo(csm);
    }

    @Test
    void specificationFromTo_returnsCorrectConnectionStatusMessage() {
        // Given
        var dataSourceInformation = new DataSourceInformation().withCountryCode("be")
                                                               .withRegionConnectorId("be-fluvius")
                                                               .withPermissionAdministratorId("fluvius")
                                                               .withMeteredDataAdministratorId("fluvius");
        var payload = new ConnectionStatusMessage().withConnectionId("cid")
                                                   .withPermissionId("pid")
                                                   .withDataNeedId("dnid")
                                                   .withDataSourceInformation(dataSourceInformation)
                                                   .withStatus(Status.ACCEPTED);
        connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(payload));
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var from = now.minusMinutes(1);
        var to = now.plusMinutes(1);
        var spec = InsertionTimeSpecification.<ConnectionStatusMessageModel>insertedAfterEquals(from)
                                             .and(InsertionTimeSpecification.insertedBeforeEquals(to));

        // When
        var res = connectionStatusMessageRepository.findAll(spec);

        // Then
        assertThat(res).hasSize(1);
    }

    @Test
    void specificationFromTo_returnsNoConnectionStatusMessage() {
        // Given
        var dataSourceInformation = new DataSourceInformation().withCountryCode("be")
                                                               .withRegionConnectorId("be-fluvius")
                                                               .withPermissionAdministratorId("fluvius")
                                                               .withMeteredDataAdministratorId("fluvius");
        var payload = new ConnectionStatusMessage().withConnectionId("cid")
                                                   .withPermissionId("pid")
                                                   .withDataNeedId("dnid")
                                                   .withDataSourceInformation(dataSourceInformation)
                                                   .withStatus(Status.ACCEPTED);
        connectionStatusMessageRepository.save(new ConnectionStatusMessageModel(payload));
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var from = now.minusDays(1);
        var to = now.minusHours(1);
        var spec = InsertionTimeSpecification.<ConnectionStatusMessageModel>insertedAfterEquals(from)
                                             .and(InsertionTimeSpecification.insertedBeforeEquals(to));

        // When
        var res = connectionStatusMessageRepository.findAll(spec);

        // Then
        assertThat(res).isEmpty();
    }
}