package org.revo.feedback.Service.Impl;

import org.revo.core.base.Doamin.UserMediaComment;
import org.revo.feedback.Repository.UserMediaCommentRepository;
import org.revo.feedback.Service.Cached.UserMediaCommentCachedService;
import org.revo.feedback.Service.UserMediaCommentService;
import org.revo.feedback.Service.UserMediaEventListener;
import org.revo.feedback.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMediaCommentServiceImpl implements UserMediaCommentService {
    @Autowired
    private UserMediaCommentRepository userMediaCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMediaCommentCachedService userMediaCommentCachedService;
    @Autowired
    private UserMediaEventListener userMediaEventListener;

    @Override
    public UserMediaComment comment(String id, String message) {
        UserMediaComment userMediaComment = new UserMediaComment();
        userMediaComment.setMediaId(id);
        userMediaComment.setMessage(message);
        UserMediaComment save = userMediaCommentRepository.save(userMediaComment);
        userMediaEventListener.AfterSaveUserMediaComment(userMediaComment);
        userMediaCommentCachedService.incComments(id);
        return save;
    }

    @Override
    public void uncomment(String id) {
        userMediaCommentRepository.deleteByIdAndUserId(id, userService.current().get());
        userMediaCommentCachedService.decComments(id);

    }

    @Override
    public List<UserMediaComment> comments(String id) {
        return userMediaCommentRepository.findAllByMediaId(id);
    }

    @Override
    public int countComments(String id) {
        return userMediaCommentRepository.countByMediaId(id);
    }
}
