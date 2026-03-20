// SPDX-FileCopyrightText: 2024-2026 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.es.datadis;

import energy.eddie.api.agnostic.RegionConnectorSecurityConfig;
import energy.eddie.regionconnector.shared.security.JwtAuthorizationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static energy.eddie.regionconnector.es.datadis.web.PermissionController.PATH_PERMISSION_ACCEPTED;
import static energy.eddie.regionconnector.es.datadis.web.PermissionController.PATH_PERMISSION_REJECTED;
import static energy.eddie.spring.regionconnector.extensions.SecurityUtils.pathPatternRequestMatcher;
import static energy.eddie.spring.regionconnector.extensions.SecurityUtils.securityFilterChain;

@RegionConnectorSecurityConfig
public class DatadisSecurityConfig {
    private static final String DATADIS_ENABLED_PROPERTY = "region-connector.es.datadis.enabled";

    @Bean
    @ConditionalOnProperty(value = DATADIS_ENABLED_PROPERTY, havingValue = "true")
    public SecurityFilterChain datadisSecurityFilterChain(
            HttpSecurity http,
            JwtAuthorizationManager jwtHeaderAuthorizationManager,
            CorsConfigurationSource corsConfigurationSource,
            ObjectMapper mapper
    ) {
        var datadisMvcRequestMatcher = pathPatternRequestMatcher(DatadisRegionConnectorMetadata.REGION_CONNECTOR_ID);
        return securityFilterChain(datadisMvcRequestMatcher,
                                   http,
                                   jwtHeaderAuthorizationManager,
                                   corsConfigurationSource,
                                   mapper,
                                   List.of(PATH_PERMISSION_ACCEPTED, PATH_PERMISSION_REJECTED),
                                   List.of());
    }
}
