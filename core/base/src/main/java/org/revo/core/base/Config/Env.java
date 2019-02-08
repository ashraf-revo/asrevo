package org.revo.core.base.Config;

import lombok.Getter;
import lombok.Setter;
import org.revo.core.base.Domain.Bucket;
import org.revo.core.base.Domain.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashraf on 17/04/17.
 */
@Getter
@Setter
@ConfigurationProperties("org.revo.env")
public class Env {
    private List<User> users = new ArrayList<>();
    private Map<String, Bucket> buckets = new HashMap<>();
}
