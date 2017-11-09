package io.pivotal.pal.tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by e018538 on 11/9/17.
 */

@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${SECURITY_FORCE_HTTPS:false}")
    private boolean forceHttps;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        if(forceHttps) {
            log.info("https on");
            http.requiresChannel()
                    .anyRequest()
                    .requiresSecure();
        } else {
            log.info("https off");
        }

        http.authorizeRequests()
                .antMatchers("/**")
                .hasRole("USER")
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}
