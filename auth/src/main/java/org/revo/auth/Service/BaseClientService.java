package org.revo.auth.Service;

import org.revo.core.base.Domain.BaseClient;

import java.util.List;
import java.util.Optional;

public interface BaseClientService {
    Optional<BaseClient> loadClientByClientId(String id);

    void save(BaseClient baseClient);

    long count();

    List<BaseClient> findAll(String id);
}
