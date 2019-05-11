package org.revo.tube.Service.Impl;

import org.revo.core.base.Domain.GCSEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Remote {
    public Mono<Void> fun1(Mono<GCSEvent> gcsEvent) {
        return WebClient
                .builder().baseUrl("https://us-central1-ivory-program-229516.cloudfunctions.net")
                .build()
                .post()
                .uri("func1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(gcsEvent, GCSEvent.class)
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
