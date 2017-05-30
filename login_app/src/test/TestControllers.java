import beans.config.AppWebSocketConfig;
import beans.config.PersistenceConfiguration;
import beans.config.WebConfig;
import beans.config.security.SecurityConfig;
import dto.DeviceDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, PersistenceConfiguration.class, AppWebSocketConfig.class})
@WebAppConfiguration
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@Transactional
public class TestControllers {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void authenticationAdminSuccess() throws Exception {
        mvc
                .perform(formLogin("/api/user/login").user("email", "anyapinchuk3@gmail.com").password("1111"))
                .andExpect(redirectedUrl("/house/all"));
    }

    @Test
    public void authenticationFailed() throws Exception {
        mvc
                .perform(formLogin("/api/user/login").user("email", "user").password("invalid"))
                .andExpect(redirectedUrl("/login?error=wrong_credentials"))
                .andExpect(unauthenticated());
    }

    @Test
    public void requestProtectedUrlWithUser() throws Exception {
        mvc
                .perform(get("/api/device/all").with(user("anyapinchuk3@gmail.com").password("1111")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateDeviceSuccessWithUser() throws Exception {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(4L);
        deviceDTO.setSecured(false);
        deviceDTO.setState("off");
        mvc
                .perform(formLogin("/api/user/login").user("email", "nutaanuta-30.10.30@mail.ru").password("1111"))
                .andExpect(redirectedUrl("/device/all"));
        mvc
                .perform(post("/api/device/update", deviceDTO)).andExpect(status().isFound());
    }
}
