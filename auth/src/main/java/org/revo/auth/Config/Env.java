package org.revo.auth.Config;

import lombok.Getter;
import lombok.Setter;
import org.revo.auth.Domain.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashraf on 17/04/17.
 */
@Getter
@Setter
@ConfigurationProperties("org.revo.env")
public class Env {
    private List<User> users = new ArrayList<>();
}
