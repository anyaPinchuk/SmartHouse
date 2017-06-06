package beans.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
@ComponentScan(basePackages = "beans",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "beans.controllers.*")
        })
public class RootConfig extends WebMvcConfigurerAdapter {
        @Bean
        public CookieLocaleResolver localeResolver() {
                CookieLocaleResolver localeResolver = new CookieLocaleResolver();
                localeResolver.setDefaultLocale(new Locale("EN"));
                localeResolver.setCookieName("locale");
                return localeResolver;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
                interceptor.setParamName("lang");
                registry.addInterceptor(interceptor);
        }

}
