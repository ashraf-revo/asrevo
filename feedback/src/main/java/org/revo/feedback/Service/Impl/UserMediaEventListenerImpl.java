package org.revo.feedback.Service.Impl;

import org.revo.core.base.Doamin.MediaInformation;
import org.revo.core.base.Doamin.UserMediaComment;
import org.revo.core.base.Doamin.UserMediaLike;
import org.revo.core.base.Doamin.UserMediaView;
import org.revo.feedback.Service.Cached.MediaInformationCachedService;
import org.revo.feedback.Service.MediaInformationService;
import org.revo.feedback.Service.UserMediaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMediaEventListenerImpl implements UserMediaEventListener {
    @Autowired
    private MediaInformationService mediaInformationService;
    @Autowired
    private MediaInformationCachedService mediaInformationCachedService;

    @Override
    public void AfterSaveUserMediaLike(UserMediaLike event) {
        MediaInformation mediaInformation = mediaInformationService.findByMediaId(event.getMediaId())
                .map(MediaInformation::incLikes)
                .orElseGet(() -> new MediaInformation().incLikes());
        mediaInformationCachedService.update(mediaInformationService.save(mediaInformation));
    }

    @Override
    public void AfterSaveUserMediaView(UserMediaView event) {
        MediaInformation mediaInformation = mediaInformationService.findByMediaId(event.getMediaId())
                .map(MediaInformation::incViews)
                .orElseGet(() -> new MediaInformation(event.getMediaId()).incViews());
        mediaInformationCachedService.update(mediaInformationService.save(mediaInformation));
    }

    @Override
    public void AfterSaveUserMediaComment(UserMediaComment event) {
        MediaInformation mediaInformation = mediaInformationService.findByMediaId(event.getMediaId())
                .map(MediaInformation::incComments)
                .orElseGet(() -> new MediaInformation().incComments());
        mediaInformationCachedService.update(mediaInformationService.save(mediaInformation));
    }
}