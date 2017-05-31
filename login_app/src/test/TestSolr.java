import beans.config.AppWebSocketConfig;
import beans.config.PersistenceConfiguration;
import beans.config.WebConfig;
import beans.config.security.SecurityConfig;
import entities.solr.SolrDevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import solr.SolrDeviceRepository;

import javax.transaction.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, PersistenceConfiguration.class, AppWebSocketConfig.class})
@WebAppConfiguration
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@Transactional
public class TestSolr {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SolrDeviceRepository repository;

    @Test
    public void findAllDocs(){
        List<SolrDevice> devices = repository.find("g", "2");
        System.out.println(devices);
    }
}
