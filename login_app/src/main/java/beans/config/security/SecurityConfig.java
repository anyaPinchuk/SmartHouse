package beans.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;
    private MyAuthenticationFailureHahdler myAuthenticationFailureHahdler;
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Autowired
    public void setMyAuthenticationFailureHahdler(MyAuthenticationFailureHahdler myAuthenticationFailureHahdler) {
        this.myAuthenticationFailureHahdler = myAuthenticationFailureHahdler;
    }

    @Autowired
    public void setCustomAuthenticationProvider(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Autowired
    public void setMySimpleUrlAuthenticationSuccessHandler(MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler) {
        this.mySimpleUrlAuthenticationSuccessHandler = mySimpleUrlAuthenticationSuccessHandler;
    }

    @Autowired
    public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers("/api/device/**").access("isAuthenticated()")
                .antMatchers("/api/user/reg").access("hasRole('ROLE_OWNER')")
                .antMatchers("/api/house/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/api/user/login")
                .successHandler(mySimpleUrlAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHahdler)
                .permitAll();
    }

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

}
