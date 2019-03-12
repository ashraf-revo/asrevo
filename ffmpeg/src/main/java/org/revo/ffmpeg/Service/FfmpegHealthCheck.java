package org.revo.ffmpeg.Service;


import org.revo.ffmpeg.Util.FfmpegUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class FfmpegHealthCheck extends AbstractHealthIndicator {
    @Autowired
    private FfmpegUtils ffmpegUtils;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            ffmpegUtils.version();
            builder.up();
        } catch (Exception e) {
            builder.down();
        }

    }
}
