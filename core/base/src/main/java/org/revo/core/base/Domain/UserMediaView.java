package org.revo.core.base.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@Document
public class UserMediaView {
    @Id
    private String id;
    @CreatedBy
    private String userId;
    private String mediaId;
    @CreatedDate
    private Date createdDate = new Date();
    @LastModifiedDate
    private Date lastViewDate = new Date();
    private int count = 0;

    public UserMediaView() {
    }

    public UserMediaView(String id, String userId, String mediaId, Date createdDate, Date lastViewDate, int count) {
        this.id = id;
        this.userId = userId;
        this.mediaId = mediaId;
        this.createdDate = createdDate;
        this.lastViewDate = lastViewDate;
        this.count = count;
    }

    @Transient
    public UserMediaView incViews() {
        this.count++;
        return this;
    }
}
