package org.revo.tube.Domain;

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
}
