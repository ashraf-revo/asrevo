package org.revo.feedback.Service;

import org.revo.feedback.Domain.UserMediaLike;

public interface UserMediaLikeService {
    boolean liked(String id);

    void unlike(String id);

    UserMediaLike like(String id);

    int countLikes(String id);
}
