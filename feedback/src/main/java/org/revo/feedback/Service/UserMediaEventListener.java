package org.revo.feedback.Service;

import org.revo.feedback.Domain.UserMediaComment;
import org.revo.feedback.Domain.UserMediaLike;
import org.revo.feedback.Domain.UserMediaView;

public interface UserMediaEventListener {

    void AfterSaveUserMediaLike(UserMediaLike event);

    void AfterSaveUserMediaView(UserMediaView event);

    void AfterSaveUserMediaComment(UserMediaComment event);
}