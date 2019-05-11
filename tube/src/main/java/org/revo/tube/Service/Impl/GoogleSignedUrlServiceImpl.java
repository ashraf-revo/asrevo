package org.revo.tube.Service.Impl;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import org.revo.core.base.Config.Env;
import org.revo.tube.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.google.cloud.storage.BlobInfo.newBuilder;

@Service
public class GoogleSignedUrlServiceImpl implements SignedUrlService {
    @Autowired
    private Storage storage;
    @Autowired
    private Env env;

    @Override
    public String getUrl(String bucket, String key) {
        if (env.getBuckets().get(bucket).isAccessible())
            return Paths.get("https://storage.googleapis.com", env.getBuckets().get(bucket).getName(), key).toString();
        return generate(bucket, key);
    }

    private String generate(String bucket, String key) {
        return storage.signUrl(newBuilder(env.getBuckets().get(bucket).getName(), key).build(), 2, TimeUnit.HOURS).toString();
    }
}
