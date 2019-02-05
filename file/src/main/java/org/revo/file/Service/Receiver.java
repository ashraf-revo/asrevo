package org.revo.file.Service;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.revo.file.Config.Processor;
import org.revo.file.Domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import java.io.IOException;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private FileService fileService;
    @Autowired
    private TempFileService tempFileService;

    @StreamListener(value = Processor.file_queue)
    public void queue(Message<File> file) throws IOException, ZipException {
        tempFileService.clear("queue");
        log.info("receive file_queue " + file.getPayload().getId());
        fileService.process(file.getPayload());
    }
}