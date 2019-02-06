package org.revo.file.Service.Impl;

import com.google.cloud.storage.Storage;
import org.revo.core.base.Config.Env;
import org.revo.file.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.google.cloud.storage.BlobId.of;
import static com.google.cloud.storage.BlobInfo.newBuilder;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
public class S3ServiceImpl implements S3Service {
    @Autowired
    private Storage storage;
    @Autowired
    private Env env;

    @Override
    public void push(String bucket, String key, File file) {
        try {
            this.storage.create(newBuilder(of(env.getBuckets().get(bucket).toString(), key)).build(), new FileInputStream(file));
        } catch (FileNotFoundException e) {

        }
    }

}
