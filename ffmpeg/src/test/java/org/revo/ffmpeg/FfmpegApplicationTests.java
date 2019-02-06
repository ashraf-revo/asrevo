package org.revo.ffmpeg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.revo.ffmpeg.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FfmpegApplicationTests {
    private @Autowired
    SignedUrlService signedUrlService;

    @Test
    public void contextLoads() {
        String generate = signedUrlService
                .generate("", "8536680.jpeg");
        System.out.println(generate);
    }

}

