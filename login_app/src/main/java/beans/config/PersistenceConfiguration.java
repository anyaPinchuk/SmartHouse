package beans.config;

import db.ManagerDB;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:environment.xml"})
@ComponentScan(basePackages = {"repository", "db"})
@EnableJpaRepositories("repository")
public class PersistenceConfiguration {
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private String additionalParams;

    @PostConstruct
    public void buildAdditionalParams() {
        final StringBuilder builder = new StringBuilder();
        org.springframework.core.env.PropertySource<?> propertySources = ((StandardEnvironment) environment)
                .getPropertySources().get("class path resource [environment.xml]");
        String prefixDbParams = ((Properties) propertySources.getSource()).getProperty("prefixDbParams");
        ManagerDB.buildAdditionalParams(((Properties) propertySources.getSource()).entrySet(),
                builder, prefixDbParams);

        additionalParams = builder.toString();
    }

    @Bean
    public DataSource dataSource(@Value("${db.url}") String dbUrl, @Value("${db.username}") String user,
                                 @Value("${db.password}") String password, @Value("${db.name}") String dbName) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(dbUrl + dbName + additionalParams);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan("entities");

        entityManagerFactoryBean.setJpaProperties(hibernateProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    private Properties hibernateProperties() {
        final String hibernatePrefix = "hibernate.";
        final Properties properties = new Properties();
        org.springframework.core.env.PropertySource<?> propertySources = ((StandardEnvironment) environment)
                .getPropertySources().get("class path resource [environment.xml]");
        ((Properties) propertySources.getSource()).entrySet().stream()
                .filter(pair -> ((String) pair.getKey()).startsWith(hibernatePrefix))
                .forEach(pair -> properties.setProperty(((String) pair.getKey()),
                        (String) pair.getValue()));
        return properties;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
