package org.revo.file.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexImpl {
    private String index;
    private String resolution;
    private Status status;
    private long execution;
}
