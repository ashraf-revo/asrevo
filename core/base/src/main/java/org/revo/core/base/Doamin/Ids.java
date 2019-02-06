package org.revo.core.base.Doamin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ids {
    public Ids() {
    }

    public Ids(List<String> ids) {
        this.ids = ids;
    }

    private List<String> ids;
}
