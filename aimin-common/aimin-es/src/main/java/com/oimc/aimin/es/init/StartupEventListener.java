package com.oimc.aimin.es.init;

import com.oimc.aimin.es.index.DrugIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupEventListener {

    private final ElasticsearchTemplate template;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        initDrugIndex();
    }

    public void initDrugIndex(){
        IndexOperations indexOps = template.indexOps(DrugIndex.class);
        if(!indexOps.exists()){
            indexOps.create();
            Document mapping = indexOps.createMapping(DrugIndex.class);
            indexOps.putMapping(mapping);
        }
    }
}
