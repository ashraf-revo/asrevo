package org.revo.auth.Service.Impl;

import org.revo.auth.Repository.UserRepository;
import org.revo.auth.Service.UserService;
import org.revo.core.base.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ashraf on 17/04/17.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    public MongoOperations mongoOperations;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailOrPhone(email, email);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void save(User user) {
        User save = userRepository.save(user);
        activate(save.getId());
    }

    @Override
    public void encodeThenSave(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        save(user);
    }

/*
    @Override
    public void activate(Long id) {
        userRepository.activate(id);
    }
*/

    @Override
    public Optional<User> findOne(String id) {
//        return Optional.of(userRepository.findOne(id));
        return userRepository.findById(id);
    }

    @Override
    public Iterable<User> findAll(List<String> ids) {
//        return userRepository.findAll(ids);
        return userRepository.findAllById(ids);
    }

    @Override
    public void activate(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id).and("type").is("000"));
        Update update = new Update();
        update.set("enable", true);
        update.set("type", "110");
        mongoOperations.updateFirst(query, update, User.class);
    }

    @Override
    public String current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) return ((User) authentication.getPrincipal()).getId();
        else return null;
    }
}
