package org.revo.ffmpeg.Config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.revo.core.base.Domain.Master;
import org.revo.ffmpeg.Service.FfmpegService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Paths.get;

/**
 * Created by ashraf on 23/04/17.
 */
@Configuration
public class Util {
    @Bean
    public FFmpegExecutor executor() {
        try {
            if (new File("/usr/bin/ffmpeg").exists()) {
                FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
                FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
                return new FFmpegExecutor(ffmpeg, ffprobe);
            } else {
//                init();
                FFprobe ffprobe = new FFprobe(System.getProperty("user.home") + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffprobe");
                FFmpeg ffmpeg = new FFmpeg(System.getProperty("user.home") + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffmpeg");
                return new FFmpegExecutor(ffmpeg, ffprobe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public FFprobe fFprobe(FFmpegExecutor executor) {
        try {
            if (new File("/usr/bin/ffmpeg").exists()) {
                return new FFprobe("/usr/bin/ffprobe");
            } else {
                return new FFprobe(System.getProperty("user.home") + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffprobe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public CommandLineRunner runner(FfmpegService ffmpegService) {
        return args -> {
            Master master = new Master();
            master.setId("5c855058c5ac870001453f28");
            master.setSecret("4f3bcdb3016649e89f7a41ff623855b9");
            master.setExt("mov");
            master.setFile("5c85505429fe00000165bdd7");
            master.setUserId("5bfd3df1ad8ce6617f9bf635");
            FFmpegProbeResult probe = ffmpegService.probe(master, get(master.getId(), master.getId()).toString());
            System.out.println(probe.getStreams().get(0).index);
        };
    }
}
