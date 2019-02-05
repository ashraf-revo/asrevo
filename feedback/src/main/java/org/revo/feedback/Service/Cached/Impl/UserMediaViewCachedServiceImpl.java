package org.revo.feedback.Service.Cached.Impl;

import org.revo.feedback.Domain.UserMediaView;
import org.revo.feedback.Repository.UserMediaViewRepository;
import org.revo.feedback.Service.Cached.UserMediaViewCachedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class UserMediaViewCachedServiceImpl implements UserMediaViewCachedService {
    @Autowired
    private UserMediaViewRepository userMediaViewRepository;
    @Autowired
    private UserMediaViewCachedService userMediaViewCachedService;
    @Autowired
    private MongoOperations mongoOperations;

    @CachePut(value = "UserMediaView", key = "#id")
    @Override
    public int incViews(String id) {
        return userMediaViewCachedService.Views(id) + 1;
    }

    @Cacheable(value = "UserMediaView", key = "#id")
    @Override
    public int Views(String id) {
        List<UserMediaView> mappedResults = mongoOperations.aggregate(newAggregation(match(where("mediaId").is(id)), group("mediaId").sum("count").as("count")), "UserMediaView", UserMediaView.class).getMappedResults();
        if (mappedResults.size() > 0) {
            int count = mappedResults.get(0).getCount();
            return count;
        } else return 0;
    }

}
