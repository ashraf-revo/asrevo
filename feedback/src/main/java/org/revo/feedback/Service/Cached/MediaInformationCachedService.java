package org.revo.feedback.Service.Cached;

import org.revo.feedback.Domain.MediaInformation;

import java.util.List;

public interface MediaInformationCachedService {
    MediaInformation get(String mediaId);

    MediaInformation update(MediaInformation mediaInformation);

    List<MediaInformation> trending(int size);

    void trending();
}
