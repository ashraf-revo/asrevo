package org.revo.auth.Config;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.buf.MessageBytes;
import org.revo.auth.Service.BaseClientService;
import org.revo.auth.Service.UserService;
import org.revo.core.base.Config.Env;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.ServletException;
import javax.validation.Validator;
import java.io.IOException;

/**
 * Created by ashraf on 17/04/17.
 */
@Configuration
public class Util {
    // copied from spring-cloud/spring-cloud-netflix#1108


    private ValveBase valveBase() {
        return new ValveBase() {
            @Override
            public void invoke(Request request, Response response) throws IOException, ServletException {
                final MessageBytes serverNameMB = request.getCoyoteRequest().serverName();
                String originalServerName = null;
                final String forwardedHost = request.getHeader("X-Forwarded-Host");
                if (forwardedHost != null) {
                    originalServerName = serverNameMB.getString();
                    serverNameMB.setString(forwardedHost);
                }
                try {
                    getNext().invoke(request, response);
                } finally {
                    if (forwardedHost != null) {
                        serverNameMB.setString(originalServerName);
                    }
                }
            }
        };
    }


    @Bean
    @Profile("prod")
    public /*EmbeddedServletContainerCustomizer*/WebServerFactoryCustomizer customizer() {
        return factory -> ((/*TomcatEmbeddedServletContainerFactory*/TomcatServletWebServerFactory) factory).addContextValves(valveBase());
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserService userService) {
        return s -> userService.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }

    @Bean
    CommandLineRunner runner(Env env, UserService userService, BaseClientService baseClientService) {
        return strings -> {
            env.getUsers().forEach(userService::save);
            env.getBaseClients().forEach(baseClientService::save);
        };
    }

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    ValidatingMongoEventListener validatingMongoEventListener(Validator validator) {
        return new ValidatingMongoEventListener(validator);
    }

}
