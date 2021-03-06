package org.revo.tube.Controller;

import lombok.extern.slf4j.Slf4j;
import org.revo.core.base.Domain.*;
import org.revo.tube.Service.FileService;
import org.revo.tube.Service.Impl.Remote;
import org.revo.tube.Service.IndexService;
import org.revo.tube.Service.MasterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
    private final String keyUrl = masterURL + "/{key}.key";

    @Bean
    public RouterFunction<ServerResponse> function(FileService fileService, Remote remote, MasterService masterService, IndexService indexService) {
        return route(POST("/api/file/save"), serverRequest -> ok().body(serverRequest.bodyToMono(File.class).flatMap(fileService::save)
                .map(GCSEvent::new)
                .flatMap(it -> remote.fun1(Mono.just(it)))
                .then(), Void.class))
                .andRoute(GET("/api/master/user/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, 1000, null, Optional.of(serverRequest.pathVariable("id")).map(it -> new Ids(Collections.singletonList(it))).orElse(new Ids()), new Ids()), Master.class))
                .andRoute(GET("/api/master/one/{id}"), serverRequest -> ok().body(masterService.findOne(serverRequest.pathVariable("id")), Master.class))
                .andRoute(GET("/api/master" + masterURL), serverRequest -> ok()
                        .header("Content-Type", "application/x-mpegURL")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                        .body(masterService.getStream(serverRequest.pathVariable("master")).map(it -> new DefaultDataBufferFactory().wrap(it.getBytes())), DataBuffer.class))
                .andRoute(GET("/api/master" + indexUrl), serverRequest -> ok()
                        .header("Content-Type", "application/x-mpegURL")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("master") + ".m3u8")
                        .body(indexService.findOneParsed(serverRequest.pathVariable("master"), serverRequest.pathVariable("index")).map(it -> new DefaultDataBufferFactory().wrap(it.getBytes())), DataBuffer.class))
                .andRoute(GET("/api/master" + keyUrl), serverRequest -> ok()
                        .header("Content-Type", "application/pgp-keys")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serverRequest.pathVariable("key") + ".key")
                        .body(masterService.findOne(serverRequest.pathVariable("key")).map(Master::getSecret).map(it -> new DefaultDataBufferFactory().wrap(it.getBytes())), DataBuffer.class))
                .andRoute(GET("/api/master/{size}/{id}"), serverRequest -> ok().body(masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id").equals("0") ? null : serverRequest.pathVariable("id"), new Ids(), new Ids()), Master.class))
                .andRoute(POST("/api/master/{size}/{id}"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, parseInt(serverRequest.pathVariable("size")), serverRequest.pathVariable("id"), it, new Ids())), Master.class))
                .andRoute(POST("/api/master/"), serverRequest -> ok().body(serverRequest.bodyToMono(Ids.class).flatMapMany(it -> masterService.findAll(Status.SUCCESS, 1000, null, new Ids(), it)), Master.class));
    }
}
