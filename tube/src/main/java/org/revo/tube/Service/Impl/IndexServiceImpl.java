package org.revo.tube.Service.Impl;

import com.comcast.viper.hlsparserj.tags.UnparsedTag;
import org.revo.core.base.Config.Env;
import org.revo.core.base.Domain.Index;
import org.revo.tube.Repository.IndexRepository;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.MasterService;
import org.revo.tube.Service.SignedUrlService;
import org.revo.tube.Util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static org.revo.tube.Util.Util.TOString;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private MasterService masterService;
    @Autowired
    private SignedUrlService signedUrlService;
    @Autowired
    private Env env;

    @Override
    public Mono<Index> save(Index index) {
        return indexRepository.save(index);
    }

    @Override
    public Mono<Index> findOne(String id) {
        return indexRepository.findById(id);
    }

    @Override
    public Flux<Index> findByMaster(String master) {
        return indexRepository.findByMaster(master);
    }

    @Override
    public Mono<Index> saveOrAppend(Index index) {
        index.setTags(index.getTags().stream().filter(it -> it.getTagName().equals("EXTINF")).collect(Collectors.toList()));
        return findOne(index.getId())
                .map(it -> {
                    it.getTags().addAll(index.getTags());
                    it.setTags(it.getTags().stream().sorted(Util::comp).collect(Collectors.toList()));
                    return it;
                })
                .defaultIfEmpty(index).flatMap(this::save);
    }

    @Override
    public Mono<String> findOneParsed(String master, String index) {
        return Mono.zip(findOne(index), masterService.findOne(master)).map(it ->
                "#EXTM3U\n" +
                        "#EXT-X-VERSION:3\n" +
                        "#EXT-X-TARGETDURATION:10\n" +
                        "#EXT-X-MEDIA-SEQUENCE:0\n" +
                        "#EXT-X-KEY:METHOD=AES-128,URI=\"" + master + ".key/\",IV=" + it.getT2().getIv() + "\n" + TOString(it.getT1().getTags(), s -> signedUrlService.getUrl("ts", get(it.getT2().getFile(), master, index, s).toString())) + "#EXT-X-ENDLIST");
    }


}
