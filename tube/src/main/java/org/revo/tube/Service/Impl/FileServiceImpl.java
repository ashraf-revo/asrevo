package org.revo.tube.Service.Impl;

import org.revo.tube.Domain.File;
import org.revo.tube.Repository.FileRepository;
import org.revo.tube.Service.FileService;
import org.revo.tube.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserService userService;

    @Override
    public Mono<File> save(File file) {
        return userService.current().flatMap(it -> {
            file.setUserId(it);
            return fileRepository.save(file);
        });
    }
}
