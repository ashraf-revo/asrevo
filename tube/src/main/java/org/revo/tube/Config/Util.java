package org.revo.tube.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

@Configuration
public class Util {

    @Bean
    public LoggingEventListener loggingEventListener() {
        return new LoggingEventListener();
    }
}
