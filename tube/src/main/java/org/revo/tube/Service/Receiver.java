package org.revo.tube.Service;

import lombok.extern.slf4j.Slf4j;
import org.revo.core.base.Doamin.Index;
import org.revo.core.base.Doamin.Master;
import org.revo.tube.Config.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private MasterService masterService;
    @Autowired
    private IndexService indexService;

    @StreamListener
    public void hls(@Input(Processor.tube_hls) Flux<Index> index) {
        index
                .doOnNext(it -> log.info("receive tube_hls " + it.getId()))
                .flatMap(it -> indexService.save(it))
                .flatMap(it -> masterService.append(Mono.just(it)))
                .subscribe();
    }

    @StreamListener
    @Output(Processor.feedback_index)
    public Flux<Master> info(@Input(Processor.tube_info) Flux<Master> master) {
        return master
                .doOnNext(it -> log.info("receive tube_info " + it.getId()))
                .flatMap(it -> masterService.saveInfo(it))
                .doOnNext(it -> log.info("send feedback_index " + it.getId()));
    }

    @StreamListener
    @Output(Processor.ffmpeg_queue)
    public Flux<Master> store(@Input(Processor.tube_store) Flux<Master> master) {
        return master
                .doOnNext(it -> log.info("receive tube_store " + it.getId()))
                .flatMap(it -> masterService.save(it))
                .doOnNext(it -> log.info("send ffmpeg_queue " + it.getId()));
    }
}
