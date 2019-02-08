package org.revo.feedback.Service;

import org.revo.core.base.Domain.Master;
import org.revo.core.base.Domain.Search;

import java.io.IOException;
import java.util.List;

public interface MasterService {
    void index(Master master);

    List<Master> search(Search search);

    void delete(String id) throws IOException;

    Master findOne(String id) throws IOException;

    String steam(String txt);

}
