package org.revo.feedback.Repository;

import org.revo.feedback.Domain.MediaInformation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaInformationRepository extends MongoRepository<MediaInformation, String> {
    Optional<MediaInformation> findByMediaId(String id);
}
