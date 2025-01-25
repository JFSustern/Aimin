package com.oimc.aimin.es.service;

import cn.hutool.core.util.ObjectUtil;
import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.exception.BusinessException;
import com.oimc.aimin.es.exception.ErrorType;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.repository.DrugIndexRepository;
import com.oimc.aimin.es.service.processor.DrugHighlightFields;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DrugEsOperationImpl implements DrugEsOperation {

    private final DrugIndexRepository drugIndexRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Iterable<DrugIndex> batchSave (List<DrugIndex> Indexes) {
        if (CollectionUtils.isEmpty(Indexes)) {
            throw new BusinessException(ErrorType.LIST_NOT_EMPTY);
        }
        return drugIndexRepository.saveAll(Indexes);
    }

    @Override
    public DrugIndex save(DrugIndex drugIndex) {
        if (ObjectUtil.isEmpty(drugIndex)) {
            throw new BusinessException(ErrorType.LIST_NOT_EMPTY);
        }
        return drugIndexRepository.save(drugIndex);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ErrorType.ID_NOT_NULL);
        }
        drugIndexRepository.deleteAllById(ids);
    }

    @Override
    public List<DrugIndex> searchDrugsWithHighlight(DrugSearchRequest request) {

        HighlightQuery highlightQuery = buildHighlightQuery();
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query ->
                        query.multiMatch(multiMatch ->
                                multiMatch.query(request.getKeyword())
                                        .fields(DrugHighlightFields.getAllFieldList())))
                .withHighlightQuery(highlightQuery)
                .withPageable(PageRequest.of(request.getPage(), request.getSize()))
                .build();

        SearchHits<DrugIndex> search = elasticsearchTemplate.search(nativeQuery, DrugIndex.class);

        search.stream().forEach(hit -> {
            Map<String, List<String>> highlightFields = hit.getHighlightFields();

            highlightFields.forEach((field, highlights) -> {
                System.out.println("命中字段: " + field);
                highlights.forEach(highlightText -> System.out.println(field + " 高亮内容: " + highlightText));
            });
        });
        List<DrugIndex> list = search.stream().map(SearchHit::getContent).toList();

        return list;
    }

    @Override
    public List<DrugIndex> fetchPage(DrugSearchRequest request) {
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withPageable(PageRequest.of(request.getPage(), request.getSize()))
                .build();
        SearchHits<DrugIndex> search = elasticsearchTemplate.search(nativeQuery, DrugIndex.class);
        return search.stream().map(SearchHit::getContent).toList();
    }

    HighlightQuery buildHighlightQuery() {
        List<String> allFieldList = DrugHighlightFields.getAllFieldList();
        List<HighlightField> highlightFieldList = new ArrayList<>();
        for(String field : allFieldList){
            highlightFieldList.add(new HighlightField(field));
        }
        Highlight highlight = new Highlight(highlightFieldList);
        return new HighlightQuery(highlight, DrugIndex.class);
    }

}
