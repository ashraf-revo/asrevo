package org.revo.core.base.Domain;

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
public class UserUserFollow {
    @Id
    private String id;
    @CreatedBy
    private String from;
    private String to;
    @CreatedDate
    private Date createdDate = new Date();

    public UserUserFollow() {
    }

    public UserUserFollow(String id, String from, String to, Date createdDate) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.createdDate = createdDate;
    }
}