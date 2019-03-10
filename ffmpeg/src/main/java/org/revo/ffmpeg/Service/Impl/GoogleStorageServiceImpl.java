package org.revo.ffmpeg.Service.Impl;

import com.google.cloud.storage.Storage;
import org.revo.core.base.Config.Env;
import org.revo.core.base.Domain.Master;
import org.revo.ffmpeg.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.cloud.storage.BlobId.of;
import static com.google.cloud.storage.BlobInfo.newBuilder;
import static java.nio.file.Paths.get;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
public class GoogleStorageServiceImpl implements StorageService {
    @Autowired
    private Env env;
    @Autowired
    private Storage storage;


    @Override
    public void push(String bucket, String key, File file) {
        try {
            this.storage.create(newBuilder(of(env.getBuckets().get(bucket).toString(), key)).build(), new FileInputStream(file));
        } catch (FileNotFoundException e) {

        } finally {
            file.delete();
        }
    }

    @Override
    public void pushTsVideo(Master master, Path parent) throws IOException {
        Files.walk(parent)
                .filter(Files::isRegularFile)
                .filter(it -> it.getFileName().toString().endsWith("ts"))
                .forEach(it -> push("ts", get(master.getFile(), master.getId(), master.getImpls().get(0).getIndex(), master.getImpls().get(0).getIndex()).toString(), it.toFile()));
    }


    @Override
    public void pushSplitedVideo(Master master, Path parent) throws IOException {
        Files.walk(parent)
                .filter(Files::isRegularFile)
                .forEach(it -> push("video", get(master.getFile(), master.getId(), master.getId(), getBaseName(it.toString())).toString(), it.toFile()));
    }
}
