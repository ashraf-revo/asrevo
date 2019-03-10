package org.revo.file.Service;

import java.io.File;

/**
 * Created by ashraf on 15/04/17.
 */
public interface StorageService {
    void push(String bucket, String key, File file);
}
