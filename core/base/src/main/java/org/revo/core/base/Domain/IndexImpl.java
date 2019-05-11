package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexImpl {
    private String index;
    private String resolution;
    private Status status;
    private long execution;

    public IndexImpl() {
    }

    public IndexImpl(String index, String resolution) {
        this.index = index;
        this.resolution = resolution;
    }

    public IndexImpl(String index, String resolution, Status status, long execution) {
        this.index = index;
        this.resolution = resolution;
        this.status = status;
        this.execution = execution;
    }
}
