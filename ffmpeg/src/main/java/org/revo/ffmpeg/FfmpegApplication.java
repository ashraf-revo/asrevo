package org.revo.ffmpeg;

import org.revo.core.base.Config.Env;
import org.revo.ffmpeg.Config.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({Processor.class})
@EnableConfigurationProperties(Env.class)
public class FfmpegApplication {

    public static void main(String[] args) {
        SpringApplication.run(FfmpegApplication.class, args);
    }

}

