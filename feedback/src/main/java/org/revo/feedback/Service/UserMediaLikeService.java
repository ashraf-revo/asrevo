package org.revo.feedback.Service;

import org.revo.core.base.Doamin.UserMediaLike;

public interface UserMediaLikeService {
    boolean liked(String id);

    void unlike(String id);

    UserMediaLike like(String id);

    int countLikes(String id);
}
