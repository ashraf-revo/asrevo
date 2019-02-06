package org.revo.tube.Repository;

import org.revo.core.base.Doamin.Index;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface IndexRepository extends ReactiveCrudRepository<Index, String> {
    Flux<Index> findByMaster(String master);
}
