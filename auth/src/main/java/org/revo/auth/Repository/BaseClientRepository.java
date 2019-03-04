package org.revo.auth.Repository;

import org.revo.core.base.Domain.BaseClient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BaseClientRepository extends CrudRepository<BaseClient, String> {
    Optional<BaseClient> findByClientId(String id);

    List<BaseClient> findByUserId(String id);
}
