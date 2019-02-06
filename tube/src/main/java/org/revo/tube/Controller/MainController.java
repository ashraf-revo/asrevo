package org.revo.tube.Controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.revo.core.base.Doamin.File;
import org.revo.core.base.Doamin.Ids;
import org.revo.core.base.Doamin.Master;
import org.revo.core.base.Doamin.Status;
import org.revo.tube.Config.Processor;
import org.revo.tube.Service.FileService;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
public class MainController {
    @Autowired
    public FileService fileService;
    @Autowired
    public Processor processor;
    @Autowired
    private MasterService masterService;
    @Autowired
    private IndexService indexService;

    private final String masterURL = "/{master}.m3u8";
    private final String indexUrl = masterURL + "/{index}.m3u8";
    private final String keyUrl = masterURL + "/{master_id}.key";


    @Bean
    public RouterFunction<ServerResponse> function() {
        return nest(path("/api/file"), route(POST("/save"), serverRequest -> ok().body(serverRequest.bodyToMono(File.class)
                        .map(it -> {
                            it.setIp(serverRequest.exchange().getRequest().getHeaders().get("X-FORWARDED-FOR").get(0));
                            return it;
                        })
                        .flatMap(it -> fileService.save(it))
                        .doOnNext(it -> processor.file_queue().send(MessageBuilder.withPayload(it).build())).then()
                , Void.class)))
                .andNest(path("/api/master"),
                        route(GET("/user/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, 1000, null, Optional.of(serverRequest.pathVariable("id")).map(it -> {
                            Ids ids = new Ids();
                            ids.setIds(Collections.singletonList(it));
                            return ids;
                        }).orElse(new Ids()), new Ids()), Master.class))
                                .andRoute(GET("/one/{id}"), serverRequest -> ok().body(masterService.findOne(serverRequest.pathVariable("id")), Master.class))
                                .andRoute(GET(masterURL), serverRequest -> ok()
                                        .header("Content-Type", "application/x-mpegURL")
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                                        .body(masterService.getStream(serverRequest.pathVariable("master")).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class))
                                .andRoute(GET(indexUrl), serverRequest -> {
                                    return ok()
                                            .header("Content-Type", "application/x-mpegURL")
                                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                                            .body(indexService.findOneParsed(serverRequest.pathVariable("master"), serverRequest.pathVariable("index")).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class);
                                })
                                .andRoute(GET(keyUrl), serverRequest -> ok()
                                        .header("Content-Type", "application/pgp-keys")
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".key")
                                        .body(masterService.findOne(serverRequest.pathVariable("master")).map(Master::getSecret).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class))
                                .andRoute(GET("/{size}/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id").equals("0") ? null : serverRequest.pathVariable("id"), new Ids(), new Ids()), Master.class))
                                .andRoute(POST("/{size}/{id}"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id"), it, new Ids())), Master.class))
                                .andRoute(POST("/"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, 1000, null, new Ids(), it)), Master.class)));
    }

}
