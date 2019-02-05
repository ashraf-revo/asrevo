package org.revo.tube.Service;

import org.revo.tube.Domain.Bucket;

public interface SignedUrlService {
    String generate(Bucket bucket, String key);
}
