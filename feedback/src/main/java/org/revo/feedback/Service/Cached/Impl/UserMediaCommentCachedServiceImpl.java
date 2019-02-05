package org.revo.feedback.Service.Cached.Impl;

import org.revo.feedback.Repository.UserMediaCommentRepository;
import org.revo.feedback.Service.Cached.UserMediaCommentCachedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserMediaCommentCachedServiceImpl implements UserMediaCommentCachedService {
    @Autowired
    private UserMediaCommentCachedService userMediaCommentCachedService;
    @Autowired
    private UserMediaCommentRepository userMediaCommentRepository;

    @CachePut(value = "UserMediaComment", key = "#id")
    @Override
    public int incComments(String id) {
        return userMediaCommentCachedService.Comments(id) + 1;
    }

    @CachePut(value = "UserMediaComment", key = "#id")
    @Override
    public int decComments(String id) {
        return userMediaCommentCachedService.Comments(id) - 1;
    }

    @Cacheable(value = "UserMediaComment", key = "#id")
    @Override
    public int Comments(String id) {
        return userMediaCommentRepository.countByMediaId(id);
    }

}
