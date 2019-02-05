package org.revo.feedback.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Master extends Base {
    private String image;
    private String secret;
    private String file;
    private double time;
    private List<IndexImpl> impls;
    private List<String> splits;
    private String resolution;
    private String keys;
}