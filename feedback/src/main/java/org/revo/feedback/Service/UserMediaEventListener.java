package org.revo.feedback.Service;

import org.revo.core.base.Domain.UserMediaComment;
import org.revo.core.base.Domain.UserMediaLike;
import org.revo.core.base.Domain.UserMediaView;

public interface UserMediaEventListener {

    void AfterSaveUserMediaLike(UserMediaLike event);

    void AfterSaveUserMediaView(UserMediaView event);

    void AfterSaveUserMediaComment(UserMediaComment event);
}