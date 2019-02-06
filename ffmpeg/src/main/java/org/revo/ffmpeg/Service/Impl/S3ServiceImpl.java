package org.revo.ffmpeg.Service.Impl;

import org.revo.core.base.Config.Env;
import org.revo.core.base.Doamin.Master;
import org.revo.ffmpeg.Service.S3Service;
import org.revo.ffmpeg.Service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
public class S3ServiceImpl implements S3Service {
    //    @Autowired
//    private AmazonS3Client amazonS3Client;
    @Autowired
    private TempFileService tempFileService;
    @Autowired
    private Env env;

    @Override
    public Path pull(String fun, String key) throws IOException {
//        S3Object object = this.amazonS3Client.getObject(env.getBuckets().get("video").toString(), key);
        Path f = tempFileService.tempFile(fun, key);
//        Files.copy(object.getObjectContent(), f);
        return f;
    }

    @Override
    public void pushMediaDelete(String key, File file) {
//        this.amazonS3Client.putObject(env.getBuckets().get("video").toString(), key, file);
        file.delete();
    }

    @Override
    public void pushImageDelete(String key, File file) {
//        this.amazonS3Client.putObject(env.getBuckets().get("thumb").toString(), key, file);
        file.delete();
    }

    @Override
    public void deleteMedia(String key) {
//        this.amazonS3Client.deleteObject(env.getBuckets().get("video").toString(), key);
    }

    @Override
    public void push(Path base, Path parent) {
        try {
            Files.walk(parent)
                    .filter(Files::isRegularFile)
                    .filter(it -> it.getFileName().toString().endsWith("ts"))
                    .forEach(it -> {
                        String key = it.toString().substring(base.toString().length() + 1);
                        saveTsDelete(it.toFile(), key);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveTsDelete(File path, String key) {
//        this.amazonS3Client.putObject(env.getBuckets().get("ts").toString(), key, path);
        path.delete();
    }

    @Override
    public void saveVideoDelete(File path, String key) {
//        this.amazonS3Client.putObject(env.getBuckets().get("video").toString(), key, path);
        path.delete();
    }

    @Override
    public void pushSplitedVideo(Master master, Path videos) throws IOException {
        Files.walk(videos)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(it -> saveVideoDelete(it, master.getFile() + "/" + master.getId() + "/" + master.getId() + "/" + getBaseName(it.toString())));
    }
}
