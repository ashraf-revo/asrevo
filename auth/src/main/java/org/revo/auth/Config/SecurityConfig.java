package org.revo.auth.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by ashraf on 10/04/17.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/403").and()
                .authorizeRequests().antMatchers("/", "/home", "/app","/app/delete/*").authenticated().anyRequest().permitAll()
                .and().formLogin().loginPage("/signin").defaultSuccessUrl("/")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/signout"))
                .and().requestMatchers().antMatchers("/403", "/home", "/app","/app/delete/*", "/", "/done", "/active/*", "/signin", "/signup", "/signout", "/oauth/authorize", "/oauth/token", "/oauth/confirm_access");

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
}
