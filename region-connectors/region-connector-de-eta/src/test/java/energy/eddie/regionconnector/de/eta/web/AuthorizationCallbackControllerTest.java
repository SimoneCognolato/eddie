package energy.eddie.regionconnector.de.eta.web;

import energy.eddie.regionconnector.de.eta.auth.AuthCallback;
import energy.eddie.regionconnector.de.eta.service.PermissionRequestAuthorizationService;
import energy.eddie.regionconnector.shared.exceptions.PermissionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AuthorizationCallbackControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionRequestAuthorizationService authorizationService;

    @InjectMocks
    private AuthorizationCallbackController controller;

    @BeforeEach
    void setUp() {
        org.springframework.web.servlet.view.InternalResourceViewResolver viewResolver = new org.springframework.web.servlet.view.InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .setViewResolvers(viewResolver)
                                 .build();
    }

    @Test
    void callback_successful_returnsOk() throws Exception {
        // Given
        doNothing().when(authorizationService).authorizePermissionRequest(any(AuthCallback.class));

        // When
        mockMvc.perform(get("/authorization-callback")
                                .queryParam("state", "pid")
                                .queryParam("token", "tokenValue"))
               // Then
               .andExpect(status().isOk())
               .andExpect(view().name("authorization-callback"))
               .andExpect(model().attribute("status", "OK"));
    }

    @Test
    void callback_withPermissionNotFound_returnsError() throws Exception {
        // Given
        doThrow(PermissionNotFoundException.class).when(authorizationService)
                                                  .authorizePermissionRequest(any(AuthCallback.class));

        // When
        mockMvc.perform(get("/authorization-callback")
                                .queryParam("state", "pid")
                                .queryParam("token", "tokenValue"))
               // Then
               .andExpect(status().isOk())
               .andExpect(view().name("authorization-callback"))
               .andExpect(model().attribute("status", "ERROR"));
    }

    @Test
    void callback_withError_returnsError() throws Exception {
        // Given
        doThrow(RuntimeException.class).when(authorizationService).authorizePermissionRequest(any(AuthCallback.class));

        // When
        mockMvc.perform(get("/authorization-callback")
                                .queryParam("state", "pid")
                                .queryParam("error", "some_error"))
               // Then
               .andExpect(status().isOk())
               .andExpect(view().name("authorization-callback"))
               .andExpect(model().attribute("status", "ERROR"));
    }
}
