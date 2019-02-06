package org.revo.core.base.Doamin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Search {
    private String search_key;
    private int page;
}
