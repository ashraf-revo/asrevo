package org.revo.core.base.Doamin;

import lombok.Builder;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Builder
@ToString
@Document
public class MediaInformation implements Serializable{
    @Id
    private String id;
    private String mediaId;
    @CreatedDate
    private Date createdDate = new Date();
    @LastModifiedDate
    private Date lastModifiedDate = new Date();
    private int viewsCount = 0;
    private int likesCount = 0;
    private int commentsCount = 0;

    public MediaInformation() {
    }

    public MediaInformation(String id, String mediaId, Date createdDate, Date lastModifiedDate, int viewsCount, int likesCount, int commentsCount) {
        this.id = id;
        this.mediaId = mediaId;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.viewsCount = viewsCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }

    public MediaInformation(String mediaId) {
        this.mediaId = mediaId;
    }

    @Transient
    public MediaInformation incViews() {
        this.viewsCount++;
        return this;
    }

    @Transient
    public MediaInformation incLikes() {
        this.likesCount++;
        return this;
    }

    @Transient
    public MediaInformation incComments() {
        this.commentsCount++;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}