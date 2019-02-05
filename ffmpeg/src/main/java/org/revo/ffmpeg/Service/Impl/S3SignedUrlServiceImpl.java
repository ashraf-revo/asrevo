package org.revo.ffmpeg.Service.Impl;

import org.revo.ffmpeg.Config.Env;
import org.revo.ffmpeg.Domain.Bucket;
import org.revo.ffmpeg.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3SignedUrlServiceImpl implements SignedUrlService {
    @Autowired
    private Env env;

    @Override
    public String generate(Bucket bucket, String key) {
        return null;
    }

    @Override
    public String getUrl(Bucket bucket, String key) {
        return null;
    }
}
