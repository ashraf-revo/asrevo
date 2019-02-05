package org.revo.feedback.Controller;

import org.revo.feedback.Domain.*;
import org.revo.feedback.Service.Cached.MediaInformationCachedService;
import org.revo.feedback.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


/**
 * Created by ashraf on 18/04/17.
 */
@RestController
@RequestMapping("api")
public class MainController {
    @Autowired
    private UserUserFollowService userUserFollowService;
    @Autowired
    private UserMediaCommentService userMediaCommentService;
    @Autowired
    private UserMediaLikeService userMediaLikeService;
    @Autowired
    private UserMediaViewService userMediaViewService;
    @Autowired
    private MediaInformationCachedService mediaInformationCachedService;
    @Autowired
    private MasterService masterService;

    @PostMapping("search")
    private SearchResult search(@RequestBody Search search) throws IOException {
        return SearchResult.builder().master(masterService.search(search)).search(search).build();
    }

    @GetMapping("/trending")
    public ResponseEntity<List<MediaInformation>> trending() {
        return ResponseEntity.ok(mediaInformationCachedService.trending(20)
                .stream().map(it -> mediaInformationCachedService.get(it.getMediaId())).collect(toList()));
    }

    @GetMapping("/user/info/{id}")
    public ResponseEntity<UserInfo> userInfo(@PathVariable("id") String id) {
        return ResponseEntity.ok(UserInfo.builder()
                .id(id)
                .followers(userUserFollowService.Followers(id))
                .following(userUserFollowService.Following(id))
                .build());
    }

    @GetMapping("/user/followers/{id}")
    public ResponseEntity<List<UserUserFollow>> userFollowers(@PathVariable("id") String id) {
        return ResponseEntity.ok(userUserFollowService.followers(id));
    }

    @GetMapping("/user/following/{id}")
    public ResponseEntity<List<UserUserFollow>> userFollowing(@PathVariable("id") String id) {
        return ResponseEntity.ok(userUserFollowService.following(id));
    }

    @GetMapping("/user/followingTo/{id}")
    public ResponseEntity<Ids> userFollowingTo(@PathVariable("id") String id) {
        return ResponseEntity.ok(new Ids(userUserFollowService.following(id).stream().map(UserUserFollow::getTo).collect(toList())));
    }

    @PostMapping("/user/info")
    public ResponseEntity<List<UserInfo>> userssInfo(@RequestBody Ids ids) {
        return ResponseEntity.ok().body(ids.getIds().stream().map(it -> UserInfo.builder()
                .id(it)
                .followers(userUserFollowService.Followers(it))
                .following(userUserFollowService.Following(it))
                .build()).collect(toList()));
    }

    @PostMapping("/media/info")
    public ResponseEntity<List<MediaInformation>> mediasInfo(@RequestBody Ids ids) {
        return ResponseEntity.ok().body(ids.getIds().stream().map(it -> mediaInformationCachedService.get(it)).collect(toList()));
    }

    @GetMapping("/media/info/{id}")
    public ResponseEntity<MediaInformation> mediaInfo(@PathVariable("id") String id) {
        return ResponseEntity.ok(mediaInformationCachedService.get(id));
    }

    @GetMapping("/media/comments/{id}")
    public ResponseEntity<List<UserMediaComment>> comments(@PathVariable("id") String id) {
        return ResponseEntity.ok(userMediaCommentService.comments(id));
    }

    @PostMapping("/media/like/{id}")
    public ResponseEntity<UserMediaLike> like(@PathVariable("id") String id) {
        return ResponseEntity.ok(userMediaLikeService.like(id));
    }

    @PostMapping("/media/liked/{id}")
    public ResponseEntity<Map<String, Boolean>> liked(@PathVariable("id") String id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("liked", userMediaLikeService.liked(id));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/media/unlike/{id}")
    public ResponseEntity unlike(@PathVariable("id") String id) {
        userMediaLikeService.unlike(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/media/view/{id}")
    public ResponseEntity<UserMediaView> view(@PathVariable("id") String id) {
        return ResponseEntity.ok(userMediaViewService.view(id));
    }

    @GetMapping("/media/views/{id}/{size}/{lastid}")
    public ResponseEntity<List<UserMediaView>> views(@PathVariable("id") String id, @PathVariable("size") int size, @PathVariable("lastid") String lastid) {
        if (lastid.equals("0")) lastid = null;
        return ResponseEntity.ok(userMediaViewService.views(id, size, lastid));
    }

    @PostMapping("/user/follow/{id}")
    public ResponseEntity<UserUserFollow> follow(@PathVariable("id") String id) {
        return ResponseEntity.ok(userUserFollowService.follow(id));
    }


    @PostMapping("/user/followed/{id}")
    public ResponseEntity<Map<String, Boolean>> followed(@PathVariable("id") String id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("followed", userUserFollowService.followed(id));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/user/unfollow/{id}")
    public ResponseEntity unfollow(@PathVariable("id") String id) {
        userUserFollowService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/media/comment/{id}")
    public ResponseEntity<UserMediaComment> comment(@PathVariable("id") String id, @RequestBody UserMediaComment userMediaComment) {
        return ResponseEntity.ok(userMediaCommentService.comment(id, userMediaComment.getMessage()));
    }

    @PostMapping("/media/uncomment/{id}")
    public ResponseEntity uncomment(@PathVariable("id") String id) {
        userMediaCommentService.uncomment(id);
        return ResponseEntity.noContent().build();
    }
}