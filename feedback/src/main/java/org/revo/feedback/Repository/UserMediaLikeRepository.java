package org.revo.feedback.Repository;

import org.revo.core.base.Doamin.UserMediaLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMediaLikeRepository extends MongoRepository<UserMediaLike, String> {
    void deleteByMediaIdAndUserId(String id, String current);

    int countByMediaId(String id);

    Optional<UserMediaLike> findByUserIdAndMediaId(String user, String media);
}
