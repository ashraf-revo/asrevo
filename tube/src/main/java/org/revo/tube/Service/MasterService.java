package org.revo.tube.Service;

import org.revo.core.base.Doamin.Ids;
import org.revo.core.base.Doamin.Index;
import org.revo.core.base.Doamin.Master;
import org.revo.core.base.Doamin.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MasterService {
    Mono<Master> saveInfo(Master master);

    Mono<Master> append(Mono<Index> index);

    Mono<Master> save(Master master);

    Mono<Master> findOne(String master);

    Mono<String> getStream(String master);

    Flux<Master> findAll(Status status, int size, String id, Ids userIds, Ids masterIds);
}
