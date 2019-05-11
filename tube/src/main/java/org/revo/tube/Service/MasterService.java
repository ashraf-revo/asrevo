package org.revo.tube.Service;

import org.revo.core.base.Domain.Ids;
import org.revo.core.base.Domain.Index;
import org.revo.core.base.Domain.Master;
import org.revo.core.base.Domain.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MasterService {
    Mono<Master> saveInfo(Master master);

    Mono<Master> append(Index index);

    Mono<Master> save(Master master);

    Mono<Master> findOne(String master);

    Mono<String> getStream(String master);

    Flux<Master> findAll(Status status, int size, String id, Ids userIds, Ids masterIds);

//    Mono<Master> publish(Index index);
}
