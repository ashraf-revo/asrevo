package org.revo.tube.Repository;

import org.revo.core.base.Domain.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File, String> {
}
