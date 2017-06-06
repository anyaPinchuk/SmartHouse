import beans.config.AppWebSocketConfig;
import beans.config.PersistenceConfiguration;
import beans.config.WebConfig;
import beans.config.security.SecurityConfig;
import entities.User;
import entities.solr.SolrDevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import repository.UserRepository;
import solr.SolrDeviceRepository;

import javax.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserRepository userRepository;

//    @Test
//    public void findAllDocs(){
//        List<SolrDevice> devices = repository.find("g", "2", new PageRequest(0, 5));
//        System.out.println(devices);
//    }

    @Test
    public void findCachedUserByLogin(){
        Map<Date, User> userMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
        for (int i = 0; i < 5; i++) {
            userMap.put(new Date(System.currentTimeMillis()), userRepository.findUserByLogin("nutaanuta-30.10.30@mail.ru"));
        }

        for (Map.Entry<Date, User> entry: userMap.entrySet()) {
            System.out.println(format.format(entry.getKey()));
        }

    }
}
