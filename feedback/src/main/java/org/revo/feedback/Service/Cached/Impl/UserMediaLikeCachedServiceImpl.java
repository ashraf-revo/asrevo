package org.revo.feedback.Service.Cached.Impl;

import org.revo.feedback.Repository.UserMediaLikeRepository;
import org.revo.feedback.Service.Cached.UserMediaLikeCachedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserMediaLikeCachedServiceImpl implements UserMediaLikeCachedService {
    @Autowired
    private UserMediaLikeRepository userMediaLikeRepository;
    @Autowired
    private UserMediaLikeCachedService userMediaLikeCachedService;

    @CachePut(value = "UserMediaLike", key = "#id")
    @Override
    public int incLikes(String id) {
        return userMediaLikeCachedService.Likes(id) + 1;
    }

    @CachePut(value = "UserMediaLike", key = "#id")
    @Override
    public int decLikes(String id) {
        return userMediaLikeCachedService.Likes(id) - 1;
    }

    @Cacheable(value = "UserMediaLike", key = "#id")
    @Override
    public int Likes(String id) {
        return userMediaLikeRepository.countByMediaId(id);
    }
}
