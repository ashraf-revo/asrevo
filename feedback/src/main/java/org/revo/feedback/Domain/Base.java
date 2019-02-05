package org.revo.feedback.Domain;

import io.searchbox.annotations.JestId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Base {
    @JestId
    private String id;
    private String title;
    private String meta;
    private String userId;
    private Date createdDate = new Date();
}