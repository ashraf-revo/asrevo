package org.revo.auth.Service;

import org.revo.core.base.Domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by ashraf on 17/04/17.
 */
public interface UserService {
    Optional<User> findByEmail(String email);

    long count();

    void save(User user);

    void encodeThenSave(User user);

//    void activate(Long id);

    Optional<User> findOne(String id);

    Iterable<User> findAll(List<String> ids);

    void activate(String id);

    String current();
}
