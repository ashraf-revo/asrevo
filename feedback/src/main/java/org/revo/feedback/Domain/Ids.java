package org.revo.feedback.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ids {
    private List<String> ids;

    public Ids() {
    }

    public Ids(List<String> ids) {
        this.ids = ids;
    }
}
