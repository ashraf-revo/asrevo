package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Master extends Base {
    private String image;
    private String secret;
    private String ext;
    private String file;
    private List<IndexImpl> impls;
    private List<String> splits;
    private double time;
    private String resolution;
    private String keys;
}