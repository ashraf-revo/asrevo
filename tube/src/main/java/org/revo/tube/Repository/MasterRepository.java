package org.revo.tube.Repository;

import org.revo.core.base.Domain.Master;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MasterRepository extends ReactiveCrudRepository<Master, String> {
}
