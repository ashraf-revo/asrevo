package org.revo.tube.Service;

import org.revo.tube.Domain.File;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<File> save(File file);
}
