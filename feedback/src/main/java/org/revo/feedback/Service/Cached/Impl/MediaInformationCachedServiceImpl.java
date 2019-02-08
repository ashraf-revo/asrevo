package org.revo.feedback.Service.Cached.Impl;

import org.revo.core.base.Domain.MediaInformation;
import org.revo.feedback.Service.Cached.MediaInformationCachedService;
import org.revo.feedback.Service.MediaInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class MediaInformationCachedServiceImpl implements MediaInformationCachedService {
    @Autowired
    private MediaInformationService mediaInformationService;
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    @Cacheable(value = "MediaInformation", key = "#mediaId", condition = "#result!=null&&result.id!=null")
    public MediaInformation get(String mediaId) {
        return mediaInformationService.findByMediaId(mediaId).orElseGet(() -> new MediaInformation(null, mediaId, null, null, 0, 0, 0));
    }

    @Override
    @CachePut(value = "MediaInformation", key = "#mediaInformation.mediaId")
    public MediaInformation update(MediaInformation mediaInformation) {
        return mediaInformation;
    }

    @Override
    @Cacheable(value = "TrendingMediaInformation", key = "#root.methodName")
    public List<MediaInformation> trending(int size) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_YEAR, -7);
        Query query = new Query();
        query.limit(size);
        query.with(new Sort(Sort.Direction.DESC, "viewsCount", "likesCount", "commentsCount"));
        query.addCriteria(Criteria.where("createdDate").gte(rightNow.getTime()));
        return mongoOperations.find(query, MediaInformation.class);
    }

    //    @CacheEvict(value = "TrendingMediaInformation", key = "#root.methodName", allEntries = true)
//    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void trending() {
    }
}
