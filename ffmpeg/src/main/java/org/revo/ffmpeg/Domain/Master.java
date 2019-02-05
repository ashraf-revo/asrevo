package org.revo.ffmpeg.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Master extends Base {
    private String image;
    private String secret;
    private String ext;
    private String file;
    private List<IndexImpl> impls;
    private List<String> splits;
    private double time;
    private String resolution;
}