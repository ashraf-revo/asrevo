package org.revo.tube.Service;

import org.revo.core.base.Domain.Index;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndexService {
    Mono<Index> save(Index index);

    Mono<String> findOneParsed(String master, String index);

    Mono<Index> findOne(String id);

    Flux<Index> findByMaster(String master);

    Mono<Index> saveOrAppend(Index index);
}
