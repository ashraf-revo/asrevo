package org.revo.tube.Service.Impl;

import com.comcast.viper.hlsparserj.tags.UnparsedTag;
import org.bson.types.ObjectId;
import org.revo.tube.Domain.*;
import org.revo.tube.Repository.MasterRepository;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.revo.tube.Util.Util.generateKey;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class MasterServiceImpl implements MasterService {
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private ReactiveMongoOperations reactiveMongoOperations;
    @Autowired
    private IndexService indexService;

    @Override
    public Mono<Master> saveInfo(Master master) {
        return masterRepository.findById(master.getId()).map(it -> {
            it.setTime(master.getTime());
            it.setResolution(master.getResolution());
            it.setImage(master.getImage());
            it.setImpls(master.getImpls());
            it.setSplits(master.getSplits());
            return it;
        }).flatMap(it -> masterRepository.save(it));
    }

    @Override
    public Mono<Master> append(Mono<Index> index) {
        return index.flatMap(itv -> findOne(itv.getMaster())
                .flatMap(it -> {
                    List<IndexImpl> indexList = it.getImpls().stream().filter(i -> i.getIndex() != null && !i.getIndex().equals(itv.getId())).collect(toList());
                    indexList.add(new IndexImpl(itv.getId(), itv.getResolution(), Status.SUCCESS, itv.getExecution()));
                    it.setImpls(indexList);
                    return masterRepository.save(it);
                }));
    }

    private static String getParsedTag(Index index) {
        UnparsedTag unparsedTag = new UnparsedTag();
        unparsedTag.setTagName("EXT-X-STREAM-INF");
        HashMap<String, String> attributes = new HashMap<>();
        if (index.getAverage_bandwidth() != null && !index.getAverage_bandwidth().trim().isEmpty())
            attributes.put("AVERAGE-BANDWIDTH", index.getAverage_bandwidth());
        if (index.getBandwidth() != null && !index.getBandwidth().trim().isEmpty())
            attributes.put("BANDWIDTH", index.getBandwidth());
        if (index.getCodecs() != null && !index.getCodecs().trim().isEmpty())
            attributes.put("CODECS", "\"" + index.getCodecs() + "\"");
        if (index.getResolution() != null && !index.getResolution().trim().isEmpty())
            attributes.put("RESOLUTION", index.getResolution().toLowerCase());
        unparsedTag.setURI(Paths.get(index.getMaster() + ".m3u8/", index.getId() + ".m3u8").toString());
        return "#" + unparsedTag.getTagName() + ":" + attributes.entrySet().stream().map(it -> it.getKey() + "=" + it.getValue()).collect(Collectors.joining(",")) + "\n" + unparsedTag.getURI() + "\n";
    }

    @Override
    public Mono<Master> save(Master master) {
        master.setSecret(generateKey());
        return masterRepository.save(master);
    }

    @Override
    public Mono<Master> findOne(String master) {
        return masterRepository.findById(master);
    }

    @Override
    public Mono<String> getStream(String master) {
        return indexService.findByMaster(master).map(MasterServiceImpl::getParsedTag).reduce("#EXTM3U\n#EXT-X-VERSION:4\n# Media Playlists\n", (x1, x2) -> x1 + x2);
    }

    @Override
    public Flux<Master> findAll(Status status, int size, String id, Ids userIds, Ids masterIds) {
        Criteria lt = where("impls.status").is(status.toString());
        if (masterIds.getIds().size() == 0)
            lt.and("id").lt(Optional.ofNullable(id).map(ObjectId::new).orElse(new ObjectId()));
        if (userIds.getIds().size() > 0) lt.and("userId").in(userIds.getIds());
        if (masterIds.getIds().size() > 0) lt.and("id").in(masterIds.getIds());
        TypedAggregation<Master> agg = newAggregation(Master.class,
                unwind("impls"),
                match(lt),
                group("id").push("impls").as("impls")
                        .first("id").as("id")
                        .first("title").as("title")
                        .first("meta").as("meta")
                        .first("userId").as("userId")
                        .first("createdDate").as("createdDate")
                        .first("image").as("image")
//                        .first("stream").as("stream")
//                        .first("secret").as("secret")
                        .first("file").as("file")
                        .first("time").as("time")
                        .first("resolution").as("resolution")
                , sort(DESC, "id")
                , limit(size));

        return reactiveMongoOperations.aggregate(agg, Master.class);
    }
}
