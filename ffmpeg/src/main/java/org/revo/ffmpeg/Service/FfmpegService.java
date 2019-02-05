package org.revo.ffmpeg.Service;

import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.revo.ffmpeg.Domain.Index;
import org.revo.ffmpeg.Domain.Master;

import java.io.IOException;

public interface FfmpegService {

    Master convert(Master master) throws IOException;

    Index hls(Master master) throws IOException;

    Master queue(Master master) throws IOException;

    Master image(Master master) throws IOException;

    Master split(Master master) throws IOException;

    FFmpegProbeResult probe(Master master, String key) throws IOException;

}
