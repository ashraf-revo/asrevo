package org.revo.tube.Service.Impl;

import org.revo.tube.Config.Env;
import org.revo.tube.Domain.Index;
import org.revo.tube.Repository.IndexRepository;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.nio.file.Paths.get;
import static org.revo.tube.Util.Util.TOString;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private IndexRepository indexRepository;
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
    public Mono<String> findOneParsed(String master, String index) {
        return findOne(index).map(it -> TOString(it.getTags(), s -> signedUrlService.generate(env.getBuckets().get("ts"), get("hls", master, index, s).toString())));
    }

}
