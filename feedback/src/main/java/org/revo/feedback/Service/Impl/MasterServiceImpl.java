package org.revo.feedback.Service.Impl;

import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.params.Parameters;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.revo.feedback.Domain.Master;
import org.revo.feedback.Service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MasterServiceImpl implements MasterService {
    @Autowired
    private JestClient client;
    private final String index = "master";
    private final String type = "master";
    private PorterStemmer porterStemmer = new PorterStemmer();
    @Autowired
    private List<String> stops;

    @Override

    public void index(Master master) {
        try {
            master.setKeys(steam(String.join(" ", master.getTitle(), master.getMeta())));
            Index index = new Index.Builder(master).index(this.index).type(type).build();
            client.execute(index);
        } catch (IOException e) {

        }
    }

    @Override
    public List<Master> search(org.revo.feedback.Domain.Search search) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("keys", steam(search.getSearch_key()).split(" ")));
        searchSourceBuilder.sort("_score", SortOrder.DESC);
        System.out.println(searchSourceBuilder.toString());
        Search build = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(index)
                .addType(type)
                .setParameter(Parameters.FROM, search.getPage() * 10)
                .setParameter(Parameters.SIZE, 10)
                .build();
        try {
            SearchResult execute = client.execute(build);
            if (execute.getResponseCode() != 400)
                return execute
                        .getHits(Master.class).stream().map(it -> it.source).collect(toList());
        } catch (IOException e) {

        }
        return Collections.emptyList();
    }

    @Override
    public void delete(String id) throws IOException {
        client.execute(new Delete.Builder(id)
                .index(index).type(type)
                .build());
    }

    @Override
    public Master findOne(String id) throws IOException {
        return client.execute(new Get.Builder(index, id).type(type).build()).getSourceAsObject(Master.class);
    }

    @Override
    public String steam(String txt) {
        String[] tokenize = SimpleTokenizer.INSTANCE.tokenize(txt.replaceAll("[-+.^:,]", " ").toLowerCase());
        return Arrays.stream(tokenize).map(porterStemmer::stem)
                .filter(it -> !stops.contains(it))
                .collect(Collectors.joining(" "));
    }
}
