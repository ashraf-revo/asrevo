package org.revo.tube.Config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Processor {
    String asrevo_hls_impl = "asrevo_hls_impl";

    @Input("asrevo_hls_impl")
    SubscribableChannel asrevo_hls_impl();

    String asrevo_new_video = "asrevo_new_video";

    @Input("asrevo_new_video")
    SubscribableChannel asrevo_new_video();
}
