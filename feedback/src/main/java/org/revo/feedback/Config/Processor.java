package org.revo.feedback.Config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Processor {
    String feedback_index = "feedback_index";

    @Input("feedback_index")
    SubscribableChannel feedback_index();

}
