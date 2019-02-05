package org.revo.tube.Domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Ids {
    public Ids() {
    }

    public Ids(List<String> ids) {
        this.ids = ids;
    }

    private List<String> ids=new ArrayList<>();
}
