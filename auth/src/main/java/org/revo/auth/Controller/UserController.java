package org.revo.auth.Controller;

import org.revo.auth.Service.UserService;
import org.revo.core.base.Doamin.Ids;
import org.revo.core.base.Doamin.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public Optional<User> user(@PathVariable("id") String id) {
        return userService.findOne(id);
    }

    @PostMapping("/user")
    public Iterable<User> user(@RequestBody Ids ids) {
        return userService.findAll(ids.getIds());
    }
}