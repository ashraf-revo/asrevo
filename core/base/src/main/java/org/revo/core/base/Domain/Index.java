package org.revo.core.base.Domain;

import com.comcast.viper.hlsparserj.tags.UnparsedTag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Index {
    @Id
    private String id;
    private String master;
    private List<UnparsedTag> tags;
    private String average_bandwidth;
    private String bandwidth;
    private String codecs;
    private String resolution;
    private long execution;
}