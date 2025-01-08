package com.oimc.aimin.es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.exception.BusinessException;
import com.oimc.aimin.es.exception.ErrorType;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.repository.DrugIndexRepository;
import com.oimc.aimin.es.service.processor.DrugHighlightFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class DrugEsOperationServiceImpl implements DrugEsOperationService {

    private final DrugIndexRepository drugIndexRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Iterable<DrugIndex> saveAll(List<DrugIndex> Indexes) {
        if (CollectionUtils.isEmpty(Indexes)) {
            throw new BusinessException(ErrorType.LIST_NOT_EMPTY);
        }
        return drugIndexRepository.saveAll(Indexes);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ErrorType.ID_NOT_NULL);
        }
        drugIndexRepository.deleteAllById(ids);
    }

    @Override
    public List<DrugIndex> searchDrugsWithHighlight(DrugSearchRequest searchRequest) {
        List<String> allFieldList = DrugHighlightFields.getAllFieldList();
        List<HighlightField> highlightFieldList = new ArrayList<>();
        for(String field : allFieldList){
            highlightFieldList.add(new HighlightField(field));
        }
        Highlight highlight = new Highlight(highlightFieldList);
        HighlightQuery highlightQuery = new HighlightQuery(highlight, DrugIndex.class);

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query ->
                        query.multiMatch(multiMatch ->
                                multiMatch.query(searchRequest.getKeyword())
                                        .fields(DrugHighlightFields.getAllFieldList())))
                .withHighlightQuery(highlightQuery)
                .build();

        SearchHits<DrugIndex> search = elasticsearchTemplate.search(nativeQuery, DrugIndex.class);

        search.stream().forEach(hit -> {
            hit.getHighlightFields().forEach((field, highlights) -> {
                highlights.forEach(highlightText -> System.out.println(field + "-----" + highlightText));
            });
            System.out.println(hit.getId());
        });
        return search.stream().map(SearchHit::getContent).toList();
    }

}
