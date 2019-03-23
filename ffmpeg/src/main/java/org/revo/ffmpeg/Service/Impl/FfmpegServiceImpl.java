package org.revo.ffmpeg.Service.Impl;

import com.comcast.viper.hlsparserj.PlaylistFactory;
import com.comcast.viper.hlsparserj.PlaylistVersion;
import com.comcast.viper.hlsparserj.tags.UnparsedTag;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.io.FilenameUtils;
import org.revo.core.base.Config.Env;
import org.revo.core.base.Domain.Index;
import org.revo.core.base.Domain.IndexImpl;
import org.revo.core.base.Domain.Master;
import org.revo.ffmpeg.Service.FfmpegService;
import org.revo.ffmpeg.Service.SignedUrlService;
import org.revo.ffmpeg.Service.StorageService;
import org.revo.ffmpeg.Util.FfmpegUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static org.revo.ffmpeg.Util.Utils.getMasterTag;
import static org.revo.ffmpeg.Util.Utils.read;

@Service
public class FfmpegServiceImpl implements FfmpegService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private FFprobe fFprobe;
    @Autowired
    private SignedUrlService signedUrlService;
    @Autowired
    private Env env;
    @Autowired
    private FfmpegUtils ffmpegUtils;

    @Override
    public Master convert(Master master) throws IOException {
        long start = System.currentTimeMillis();
        IndexImpl index = master.getImpls().get(0);
        Path converted = ffmpegUtils.doConversion(probe(master, get(master.getId(), master.getId()).toString()), index);
        index.setExecution(System.currentTimeMillis() - start);
        storageService.push("video", getPath(master, get(index.getIndex(), index.getIndex()).toString()), converted.toFile());
        master.setImpls(Collections.singletonList(index));
        return master;
    }

    @Override
    public Index hls(Master master) throws IOException {
        IndexImpl im = master.getImpls().get(0);
        Path converted = ffmpegUtils.hlsDoConversion(probe(master, get(im.getIndex(), im.getIndex()).toString()), master);
        Index index = new Index();
        index.setMaster(master.getId());
        index.setId(im.getIndex());
        index.setExecution(im.getExecution());
        Optional<UnparsedTag> masterTag = getMasterTag(converted.getParent().resolve(master.getId() + ".m3u8"));
        masterTag.ifPresent(it -> {
            index.setTags(PlaylistFactory.parsePlaylist(PlaylistVersion.TWELVE, read(converted)).getTags());
            index.setAverage_bandwidth(it.getAttributes().get("AVERAGE-BANDWIDTH"));
            index.setBandwidth(it.getAttributes().get("BANDWIDTH"));
            index.setCodecs(it.getAttributes().get("CODECS"));
            index.setResolution(it.getAttributes().get("RESOLUTION"));
        });
        storageService.pushTsVideo(master, converted);
        return index;
    }

    @Override
    public Master queue(Master master) throws IOException {
        return ffmpegUtils.info(probe(master, get(master.getId(), master.getId()).toString()), master);
    }

    @Override
    public Master image(Master master) throws IOException {
        for (Path png : ffmpegUtils.image(probe(master, get(master.getId(), master.getSplits().get((master.getSplits().size() / 2))).toString()), master.getId(), "png")) {
            File file = png.toFile();
            storageService.push("thumb", getPath(master, master.getId() + ".png"), file);
        }
/*
        for (Path jpeg : ffmpegUtils.image(probe, master.getId(), "jpeg")) {
            File file = jpeg.toFile();
            storageService.pushImageDelete(master.getId() + "/" + jpeg.getFileName().toString(), file);
        }
*/
/*
        for (Path png : ffmpegUtils.image(probe(master, get(master.getId(), master.getSplits().get((master.getSplits().size() / 2))).toString()), master.getId(), "webp")) {
            File file = png.toFile();
            storageService.push("thumb", getPath(master, master.getId() + ".webp"), file);
        }
*/
        master.setImage(signedUrlService.getUrl("thumb", getPath(master, master.getId())));
        return master;
    }

    @Override
    public Master split(Master master) throws IOException {
        Path videos = ffmpegUtils.split(probe(master, get(master.getId(), master.getId()).toString()), master);
        master.setSplits(Files.walk(videos).filter(Files::isRegularFile).map(Path::toString).map(FilenameUtils::getBaseName).collect(Collectors.toList()));
        storageService.pushSplitedVideo(master, videos);
        return master;
    }

    @Override
    public FFmpegProbeResult probe(Master master, String key) throws IOException {
        return fFprobe.probe(signedUrlService.getUrl("video", getPath(master, key)));
    }

    private String getPath(Master master, String key) {
        return get(master.getFile(), master.getId(), key).toString();
    }
}
