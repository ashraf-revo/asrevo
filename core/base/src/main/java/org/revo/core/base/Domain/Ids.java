package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Ids {
    public Ids() {
        this.ids = new ArrayList<>();
    }

    public Ids(List<String> ids) {
        this.ids = ids;
    }

    private List<String> ids;
}
