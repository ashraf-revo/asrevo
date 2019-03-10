package org.revo.file.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.bson.types.ObjectId;
import org.revo.core.base.Domain.File;
import org.revo.core.base.Domain.Master;
import org.revo.file.Config.Processor;
import org.revo.file.Service.FileService;
import org.revo.file.Service.StorageService;
import org.revo.file.Service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.Files.exists;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.revo.file.Util.FileUtil.download;
import static org.revo.file.Util.FileUtil.walk;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private Processor processor;
    @Autowired
    private TempFileService tempFileService;

    @Override
    public void process(File file) throws ZipException, IOException {
        if (file.getUrl() != null && !file.getUrl().isEmpty()) {
            Path stored = store("queue", file);
            if (stored != null && exists(stored)) {
                walk(stored)
                        .collect(Collectors.toMap(Function.identity(), it -> {
                            Master master = new Master();
                            master.setId(new ObjectId().toString());
                            master.setUserId(file.getUserId());
                            master.setTitle(file.getTitle());
                            master.setMeta(file.getMeta());
                            master.setFile(file.getId());
                            master.setExt(getExtension(it.toString()));
                            return master;
                        })).entrySet().forEach(it -> {
                    storageService.push("video", file.getId() + "/" + it.getValue().getId() + "/" + it.getValue().getId() + "/" + it.getValue().getId(), it.getKey().toFile());
                    log.info("send tube_store " + it.getValue().getId());
                    processor.tube_store().send(MessageBuilder.withPayload(it.getValue()).build());
                });
            }
        }
    }

    @Override
    public Path store(String fun, File file) {
        try {
            Path tempFile = tempFileService.tempFile("queue", getName(new URL(file.getUrl()).getPath()));
//            copyURLToFile(new URL(file.getUrl()), tempFile.toFile());
            download(file, tempFile.toFile());
            return tempFile;
        } catch (IOException e) {
            log.info("error " + e.getMessage());
            return null;
        }
    }

}
