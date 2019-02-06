package org.revo.ffmpeg.Service;


public interface SignedUrlService {
    String generate(String bucket, String key);
}
