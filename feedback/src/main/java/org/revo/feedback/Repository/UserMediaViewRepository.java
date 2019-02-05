package org.revo.feedback.Repository;

import org.revo.feedback.Domain.UserMediaView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMediaViewRepository extends MongoRepository<UserMediaView, String> {
    Optional<UserMediaView> findByMediaIdAndUserId(String id, String userId);
}
