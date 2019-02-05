package org.revo.tube.Repository;

import org.revo.tube.Domain.Master;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MasterRepository extends ReactiveCrudRepository<Master, String> {
}
