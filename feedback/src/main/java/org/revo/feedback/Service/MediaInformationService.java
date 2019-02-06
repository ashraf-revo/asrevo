package org.revo.feedback.Service;

import org.revo.core.base.Doamin.MediaInformation;

import java.util.List;
import java.util.Optional;

public interface MediaInformationService {
    Optional<MediaInformation> findByMediaId(String mediaId);

    MediaInformation save(MediaInformation mediaInformation);

    List<MediaInformation> findAll();
}
