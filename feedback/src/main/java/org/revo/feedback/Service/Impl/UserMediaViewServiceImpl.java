package org.revo.feedback.Service.Impl;

import org.revo.core.base.Domain.UserMediaView;
import org.revo.feedback.Repository.UserMediaViewRepository;
import org.revo.feedback.Service.Cached.UserMediaViewCachedService;
import org.revo.feedback.Service.UserMediaEventListener;
import org.revo.feedback.Service.UserMediaViewService;
import org.revo.feedback.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class UserMediaViewServiceImpl implements UserMediaViewService {
    @Autowired
    private UserMediaViewRepository userMediaViewRepository;
    @Autowired
    private UserMediaViewCachedService userMediaViewCachedService;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private UserMediaEventListener userMediaEventListener;
    @Autowired
    private UserService userService;

    @Override
    public UserMediaView view(String id) {
        UserMediaView userMediaView = userMediaViewRepository.findByMediaIdAndUserId(id, userService.current().get())
                .map(UserMediaView::incViews)
                .map(userMediaViewRepository::save)
                .orElseGet(() ->
                        userMediaViewRepository.save(UserMediaView.builder().mediaId(id).count(1).build())
                );
        userMediaEventListener.AfterSaveUserMediaView(userMediaView);
        userMediaViewCachedService.incViews(id);
        return userMediaView;
    }

    @Override
    public int countViews(String id) {
        List<UserMediaView> mappedResults = mongoOperations.aggregate(newAggregation(match(where("mediaId").is(id)), group("media").sum("count").as("count")), "userMediaView", UserMediaView.class).getMappedResults();
        if (mappedResults.size() > 0) return mappedResults.get(0).getCount();
        else return 0;
    }

    @Override
    public List<UserMediaView> views(String id, int size, String lastid) {
        Query query = new Query();
        if (lastid != null && !lastid.trim().isEmpty()) {
            query.addCriteria(where("lastViewDate").lt(userMediaViewRepository.findById(lastid).get().getLastViewDate()));
        }
        query.addCriteria(where("userId").is(id));
        query.with(new Sort(Sort.Direction.DESC, "lastViewDate"));
        query.limit(size);
        return mongoOperations.find(query, UserMediaView.class);
    }
}