package org.revo.tube.Service;

import com.comcast.viper.hlsparserj.PlaylistVersion;
import lombok.extern.slf4j.Slf4j;
import org.revo.core.base.Domain.GCSEvent;
import org.revo.core.base.Domain.Index;
import org.revo.core.base.Domain.IndexImpl;
import org.revo.core.base.Domain.Master;
import org.revo.tube.Config.Processor;
import org.revo.tube.Util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import reactor.core.publisher.Flux;

import java.util.AbstractMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.comcast.viper.hlsparserj.PlaylistFactory.parsePlaylist;
import static java.nio.file.Paths.get;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private MasterService masterService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private SignedUrlService signedUrlService;
    @Autowired
    private FileService fileService;

    @StreamListener(Processor.asrevo_new_video)
    public void new_video(Flux<GCSEvent> event) {
        event
                .map(it -> new AbstractMap.SimpleEntry<>(((Function<String, String>) s -> it.getName().split("/")[1]).apply(it.getName()), it))
                .map(it -> {
                    Master master = new Master();
                    master.setId(it.getKey());
                    master.setSecret(it.getValue().getMeta().getKey());
                    master.setIv(Util.convertStringToHex(it.getValue().getMeta().getIv()));
                    master.setTime(it.getValue().getTime());
                    master.setFile(it.getValue().getName().split("/")[0]);
                    master.setSplits(it.getValue().getResult());
                    master.setImage(signedUrlService.getUrl("thumb", get(master.getFile(), master.getId(), master.getId(), master.getId()).toString()));
                    master.setResolution(it.getValue().getResolution().getWidth() + "x" + it.getValue().getResolution().getHeight());
                    master.setImpls(it.getValue().getImpls().stream().map(iv -> new IndexImpl(iv.getId(), iv.getWidth() + "x" + iv.getHeight())).collect(Collectors.toList()));
                    return master;
                })
                .flatMap(it -> fileService.findOne(it.getFile()).map(itf -> {
                    it.setUserId(itf.getUserId());
                    return it;
                }))
                .flatMap(master -> masterService.save(master))
                .subscribe();
    }

    @StreamListener(Processor.asrevo_hls_impl)
    public void hls_impl(Flux<GCSEvent> event) {
        event
                .map(it -> new AbstractMap.SimpleEntry<>(((Function<String, String>) s -> it.getName().split("/")[2]).apply(it.getName()), it))
                .map(it -> {
                    Index index = new Index();
                    index.setId(it.getKey());
                    index.setMaster(it.getValue().getName().split("/")[1]);
                    index.setTags(parsePlaylist(PlaylistVersion.TWELVE, it.getValue().getTags().getIndex()).getTags().stream().filter(itt -> itt.getTagName().equals("EXTINF")).collect(Collectors.toList()));
                    parsePlaylist(PlaylistVersion.TWELVE, it.getValue().getTags().getMaster())
                            .getTags()
                            .stream()
                            .filter(itm -> itm.getTagName().equalsIgnoreCase("EXT-X-STREAM-INF"))
                            .findAny()
                            .ifPresent(itm -> {
                                index.setAverage_bandwidth(itm.getAttributes().get("AVERAGE-BANDWIDTH"));
                                index.setBandwidth(itm.getAttributes().get("BANDWIDTH"));
                                index.setCodecs(itm.getAttributes().get("CODECS"));
                                index.setResolution(itm.getAttributes().get("RESOLUTION"));
                            });
                    return index;
                })
                .flatMap(it -> indexService.save(it))
//                .flatMap(it -> indexService.saveOrAppend(it))
//                .doOnEach(it -> masterService.publish(it.get()).subscribe())
                .doOnEach(it -> masterService.append(it.get()).subscribe())
                .subscribe(it -> System.out.println(it.getId()));
    }

}
