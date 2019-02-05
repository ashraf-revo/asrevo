package org.revo.feedback.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchResult {
    private List<Master> master;
    private Search search;
}
