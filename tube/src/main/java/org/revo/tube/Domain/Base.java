package org.revo.tube.Domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
public class Base {
    @Id
    private String id;
    private String title;
    private String meta;
    private String userId;
    @CreatedDate
    private Date createdDate = new Date();
}