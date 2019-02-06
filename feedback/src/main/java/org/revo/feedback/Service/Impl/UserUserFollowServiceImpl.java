package org.revo.feedback.Service.Impl;

import org.revo.core.base.Doamin.UserUserFollow;
import org.revo.feedback.Repository.UserUserFollowRepository;
import org.revo.feedback.Service.UserService;
import org.revo.feedback.Service.UserUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserUserFollowServiceImpl implements UserUserFollowService {
    @Autowired
    private UserUserFollowRepository userUserFollowRepository;
    @Autowired
    private UserService userService;

    @Override
    public int Followers(String id) {
        return userUserFollowRepository.countByTo(id);
    }

    @Override
    public int Following(String id) {
        return userUserFollowRepository.countByFrom(id);
    }

    @Override
    public UserUserFollow follow(String id) {
        return userUserFollowRepository.findByFromAndTo(userService.current().get(), id).orElseGet(() -> userUserFollowRepository.save(UserUserFollow.builder().to(id).build()));
    }

    @Override
    public void unfollow(String id) {
        userUserFollowRepository.deleteByFromAndTo(userService.current().get(), id);
    }


    @Override
    public List<UserUserFollow> followers(String id) {
        return userUserFollowRepository.findByTo(id);
    }

    @Override
    public List<UserUserFollow> following(String id) {
        return userUserFollowRepository.findByFrom(id);
    }

    @Override
    public boolean followed(String id) {
        return userUserFollowRepository.findByFromAndTo(userService.current().get(), id).isPresent();
    }
}
