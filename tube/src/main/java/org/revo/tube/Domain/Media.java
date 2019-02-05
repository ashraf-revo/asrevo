package org.revo.tube.Domain;
/*

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import static org.revo.Util.Util.stream;

*/
/**
 * Created by ashraf on 15/04/17.
 *//*

@Getter
@Setter
@Document
public class Media {
    @Id
    private String id;
    private String title;
    private String image;
    private String masterPlaylist = stream;
    private double time;
    private String meta;
    private String secret;
    private Status status = Status.BINDING;
    @CreatedBy
    private String userId;
    @Transient
    private User user;
    @Transient
    private MediaInfo mediaInfo;
    @CreatedDate
    private Date createdDate = new Date();
    private Date publishDate = new Date();
    private String url;
    private String group = null;
}*/
