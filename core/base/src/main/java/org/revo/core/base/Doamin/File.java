package org.revo.core.base.Doamin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class File extends Base {
    private String url;
    private String ip;
}