package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document
public class UserMediaComment {
    @Id
    private String id;
    @CreatedBy
    private String userId;
    @Transient
    private User user;
    private String mediaId;
    @CreatedDate
    private Date createdDate = new Date();
    private String message;

    public UserMediaComment() {
    }

    public UserMediaComment(String id, String userId, User user, String mediaId, Date createdDate, String message) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.mediaId = mediaId;
        this.createdDate = createdDate;
        this.message = message;
    }
}
