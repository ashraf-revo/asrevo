package org.revo.feedback.Service;

import org.revo.core.base.Doamin.UserMediaComment;
import org.revo.core.base.Doamin.UserMediaLike;
import org.revo.core.base.Doamin.UserMediaView;

public interface UserMediaEventListener {

    void AfterSaveUserMediaLike(UserMediaLike event);

    void AfterSaveUserMediaView(UserMediaView event);

    void AfterSaveUserMediaComment(UserMediaComment event);
}