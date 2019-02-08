package org.revo.feedback.Repository;

import org.revo.core.base.Domain.UserUserFollow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserUserFollowRepository extends MongoRepository<UserUserFollow, String> {

    int countByFrom(String id);

    List<UserUserFollow> findByFrom(String id);

    int countByTo(String current);

    List<UserUserFollow> findByTo(String current);

    void deleteByFromAndTo(String current, String id);

    Optional<UserUserFollow> findByFromAndTo(String current, String id);
}
