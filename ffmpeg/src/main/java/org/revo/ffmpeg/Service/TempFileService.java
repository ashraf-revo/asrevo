package org.revo.ffmpeg.Service;

import java.io.IOException;
import java.nio.file.Path;

public interface TempFileService {
    Path mkdir(Path path);

    Path tempDir(String fun) throws IOException;

    Path tempFile(String fun) throws IOException;

    Path tempFile(String fun, String name);

    void clear(String fun);
}
