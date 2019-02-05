package org.revo.feedback.Config;

import io.searchbox.client.JestClient;
import io.searchbox.indices.CreateIndex;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Configuration
public class Util {
    @Bean
    public CommandLineRunner runner(JestClient client) {
        return args -> client.execute(new CreateIndex.Builder("master").build());
    }

    @Bean
    public List<String> stops() {
        try {
            return Files.readAllLines(Paths.get(new ClassPathResource("").getClassLoader().getResource("static/stop.txt").getFile()));
        } catch (IOException e) {
        }
        return Arrays.asList();
    }
}
