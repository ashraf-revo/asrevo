package org.revo.tube.Repository;

import org.revo.tube.Domain.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File, String> {
}
