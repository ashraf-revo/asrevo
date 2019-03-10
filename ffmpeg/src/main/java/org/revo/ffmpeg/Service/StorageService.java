package org.revo.ffmpeg.Service;

import org.revo.core.base.Domain.Master;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by ashraf on 15/04/17.
 */
public interface StorageService {
    void push(String bucket, String key, File file);

    void pushTsVideo(Master master, Path parent) throws IOException;

    void pushSplitedVideo(Master master, Path parent) throws IOException;
}
