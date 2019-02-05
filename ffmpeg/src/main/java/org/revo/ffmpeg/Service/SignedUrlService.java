package org.revo.ffmpeg.Service;


import org.revo.ffmpeg.Domain.Bucket;

public interface SignedUrlService {
    String generate(Bucket bucket, String key);

    String getUrl(Bucket bucket, String key);
}
