package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ManagerDB {

    protected static final Logger LOG = LoggerFactory.getLogger(ManagerDB.class);

    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.name}")
    private String dbName;
    private final String prefixDbParams;

    private Environment environment;

    public ManagerDB(@Value("${prefixDbParams}") String prefixDbParams) {
        this.prefixDbParams = prefixDbParams;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public static void buildAdditionalParams(Set<Map.Entry<Object, Object>> properties,
                                             StringBuilder builder, String prefixDbParams) {
        properties.stream()
                .filter(pair -> ((String) pair.getKey()).startsWith(prefixDbParams))
                .forEach(pair -> {
                    if (builder.length() == 0)
                        builder.append('?');
                    else builder.append('&');
                    String key = (String) pair.getKey();
                    builder.append(key.substring(prefixDbParams.length())).append('=').append(pair.getValue());
                });
    }

    @PostConstruct
    private void buildAdditionalParams() {
        final StringBuilder builder = new StringBuilder();
        PropertySource<?> propertySources = ((StandardEnvironment) environment).getPropertySources()
                .get("class path resource [environment.xml]");
        buildAdditionalParams(((Properties) propertySources.getSource()).entrySet(), builder, prefixDbParams);
        dbUrl += builder.toString();
    }
}
