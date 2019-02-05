package org.revo.tube.Service.Impl;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.revo.tube.Domain.Bucket;
import org.revo.tube.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SignedUrlServiceImpl implements SignedUrlService {
    @Autowired
    private Storage storage;

    @Override
    public String generate(Bucket bucket, String key) {
        return storage.signUrl(BlobInfo.newBuilder(bucket.getName(), key).build(), 2, TimeUnit.HOURS).toString();
    }
}
