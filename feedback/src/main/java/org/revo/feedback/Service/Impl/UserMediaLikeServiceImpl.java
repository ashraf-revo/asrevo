package org.revo.feedback.Service.Impl;

import org.revo.core.base.Doamin.UserMediaLike;
import org.revo.feedback.Repository.UserMediaLikeRepository;
import org.revo.feedback.Service.Cached.UserMediaLikeCachedService;
import org.revo.feedback.Service.UserMediaEventListener;
import org.revo.feedback.Service.UserMediaLikeService;
import org.revo.feedback.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMediaLikeServiceImpl implements UserMediaLikeService {
    @Autowired
    private UserMediaLikeRepository userMediaLikeRepository;
    @Autowired
    private UserMediaLikeCachedService userMediaLikeCachedService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMediaEventListener userMediaEventListener;

    @Override
    public boolean liked(String id) {
        return userMediaLikeRepository.findByUserIdAndMediaId(userService.current().get(), id).isPresent();
    }

    @Override
    public void unlike(String id) {
        userMediaLikeRepository.deleteByMediaIdAndUserId(id, userService.current().get());
        userMediaLikeCachedService.decLikes(id);
    }

    @Override
    public UserMediaLike like(String id) {
        return userMediaLikeRepository.findByUserIdAndMediaId(userService.current().get(), id).orElseGet(() -> {
            UserMediaLike save = userMediaLikeRepository.save(UserMediaLike.builder().mediaId(id).build());
            userMediaLikeCachedService.incLikes(id);
            userMediaEventListener.AfterSaveUserMediaLike(save);
            return save;
        });
    }

    @Override
    public int countLikes(String id) {
        return userMediaLikeRepository.countByMediaId(id);
    }
}