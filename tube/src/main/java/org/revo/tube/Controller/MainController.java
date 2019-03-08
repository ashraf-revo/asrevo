package org.revo.tube.Controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.revo.core.base.Domain.File;
import org.revo.core.base.Domain.Ids;
import org.revo.core.base.Domain.Master;
import org.revo.core.base.Domain.Status;
import org.revo.tube.Config.Processor;
import org.revo.tube.Service.FileService;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.MasterService;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
public class MainController {
    private final String masterURL = "/{master}.m3u8";
    private final String indexUrl = masterURL + "/{index}.m3u8";
    private final String keyUrl = masterURL + "/{master_id}.key";

    @Bean
    public RouterFunction<ServerResponse> function(FileService fileService, Processor processor, MasterService masterService, IndexService indexService) {
        return route(POST("/api/file/save"), serverRequest -> ok().body(serverRequest.bodyToMono(File.class).flatMap(fileService::save).doOnNext(it -> processor.file_queue().send(MessageBuilder.withPayload(it).build())).then(), Void.class))
                .andRoute(GET("/api/master/user/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, 1000, null, Optional.of(serverRequest.pathVariable("id")).map(it -> new Ids(Collections.singletonList(it))).orElse(new Ids()), new Ids()), Master.class))
                .andRoute(GET("/api/master/one/{id}"), serverRequest -> ok().body(masterService.findOne(serverRequest.pathVariable("id")), Master.class))
                .andRoute(GET("/api/master" + masterURL), serverRequest -> ok()
                        .header("Content-Type", "application/x-mpegURL")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                        .body(masterService.getStream(serverRequest.pathVariable("master")).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class))
                .andRoute(GET("/api/master" + indexUrl), serverRequest -> ok()
                        .header("Content-Type", "application/x-mpegURL")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                        .body(indexService.findOneParsed(serverRequest.pathVariable("master"), serverRequest.pathVariable("index")).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class))
                .andRoute(GET("/api/master" + keyUrl), serverRequest -> ok()
                        .header("Content-Type", "application/pgp-keys")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".key")
                        .body(masterService.findOne(serverRequest.pathVariable("master")).map(Master::getSecret).map(its -> IOUtils.toInputStream(its, Charset.defaultCharset())).map(InputStreamResource::new), InputStreamResource.class))
                .andRoute(GET("/api/master/{size}/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id").equals("0") ? null : serverRequest.pathVariable("id"), new Ids(), new Ids()), Master.class))
                .andRoute(POST("/api/master/{size}/{id}"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id"), it, new Ids())), Master.class))
                .andRoute(POST("/api/master/"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, 1000, null, new Ids(), it)), Master.class));
    }

}
