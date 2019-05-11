package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GCSEvent {
    private String bucket;
    private String name;
    private double time;
    private String pattern;
    private Resolution resolution;
    private Meta meta;
    private File file;
    private List<Resolution> Impls;
    private List<String> result;
    private Tags tags;

    public GCSEvent() {
    }

    public GCSEvent(File file) {
        this.file = file;
        file.setCreatedDate(null);
    }
}
