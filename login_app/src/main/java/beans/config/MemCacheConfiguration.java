package beans.config;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AddressProvider;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheClientFactory;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.spring.SSMCache;
import com.google.code.ssm.spring.SSMCacheManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

@Configuration
@EnableCaching
public class MemCacheConfiguration{

    private static final String memCachedUrl = "127.0.0.1:11211";

    @Bean
    public CacheManager cacheManager() {
        SSMCacheManager result = new SSMCacheManager();
        try {
            result.setCaches(Arrays.asList(
                    new SSMCache(defaultCacheFactory().getObject(), 60*5)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Bean
    public CacheFactory defaultCacheFactory() {
        CacheFactory factory = new CacheFactory();
        factory.setCacheName("defaultCache");
        factory.setAddressProvider(addressProvider());
        factory.setCacheClientFactory(cacheClientFactory());
        factory.setConfiguration(cacheConfiguration());
        return factory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CacheClientFactory cacheClientFactory() {
        return new MemcacheClientFactoryImpl();
    }

    @Bean
    public AddressProvider addressProvider() {
        return new DefaultAddressProvider(memCachedUrl);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public com.google.code.ssm.providers.CacheConfiguration cacheConfiguration() {
        com.google.code.ssm.providers.CacheConfiguration configuration =
                new com.google.code.ssm.providers.CacheConfiguration();
        configuration.setConsistentHashing(true);
        return configuration;
    }


}
