package org.revo.tube;

import org.revo.core.base.Config.Env;
import org.revo.tube.Config.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableMongoAuditing
@EnableBinding({Processor.class})
@EnableConfigurationProperties(Env.class)
public class TubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TubeApplication.class, args);
    }
}
