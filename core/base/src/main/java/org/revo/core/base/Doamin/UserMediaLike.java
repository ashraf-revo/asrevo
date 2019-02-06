package org.revo.core.base.Doamin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@Document
public class UserMediaLike {
    @Id
    private String id;
    @CreatedBy
    private String userId;
    private String mediaId;
    @CreatedDate
    private Date createdDate = new Date();

    public UserMediaLike() {
    }

    public UserMediaLike(String id, String userId, String mediaId, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.mediaId = mediaId;
        this.createdDate = createdDate;
    }
}
