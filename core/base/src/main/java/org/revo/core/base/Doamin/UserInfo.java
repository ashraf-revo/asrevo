package org.revo.core.base.Doamin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserInfo {
    private String id;
    private int followers;
    private int following;

    public UserInfo() {
    }

    public UserInfo(String id, int followers, int following) {
        this.id = id;
        this.followers = followers;
        this.following = following;
    }
}
