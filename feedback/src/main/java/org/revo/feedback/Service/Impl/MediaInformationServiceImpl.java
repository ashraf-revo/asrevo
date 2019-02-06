package org.revo.feedback.Service.Impl;

import org.revo.core.base.Doamin.MediaInformation;
import org.revo.feedback.Repository.MediaInformationRepository;
import org.revo.feedback.Service.MediaInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaInformationServiceImpl implements MediaInformationService {
    @Autowired
    private MediaInformationRepository mediaInformationRepository;

    @Override
    public Optional<MediaInformation> findByMediaId(String mediaId) {
        return mediaInformationRepository.findByMediaId(mediaId);
    }

    @Override
    public MediaInformation save(MediaInformation mediaInformation) {
        return mediaInformationRepository.save(mediaInformation);
    }

    @Override
    public List<MediaInformation> findAll() {
        return mediaInformationRepository.findAll();
    }
}
