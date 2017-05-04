package beans.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;
    private MyAuthenticationFailureHahdler myAuthenticationFailureHahdler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Autowired
    public void setCustomAuthenticationProvider(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Autowired
    public void setMySimpleUrlAuthenticationSuccessHandler(MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler) {
        this.mySimpleUrlAuthenticationSuccessHandler = mySimpleUrlAuthenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers("/device/**").access("isAuthenticated()")
                .antMatchers("/api/user/reg").access("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
                .antMatchers( "/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("email")    // Указываем параметры логина и пароля с формы логина
                .passwordParameter("password")
                .loginProcessingUrl("/api/user/login")   // указываем action с формы логина
                .successHandler(mySimpleUrlAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHahdler)
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {          // password ciphering
        return new BCryptPasswordEncoder();
    }

}
