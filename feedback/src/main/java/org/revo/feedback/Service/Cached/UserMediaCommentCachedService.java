package org.revo.feedback.Service.Cached;

public interface UserMediaCommentCachedService {
    int incComments(String id);

    int decComments(String id);

    int Comments(String id);
}
