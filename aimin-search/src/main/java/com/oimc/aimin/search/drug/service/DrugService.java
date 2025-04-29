package com.oimc.aimin.search.drug.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oimc.aimin.base.model.drug.index.DrugIndex;
import com.oimc.aimin.base.response.PageResp;
import com.oimc.aimin.search.drug.constants.DrugHighlightFields;
import com.oimc.aimin.search.drug.model.request.DrugPageRequest;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 *
 * @author 渣哥
 */
@Service
@RequiredArgsConstructor
public class DrugService {

    private final ElasticsearchTemplate elasticsearchTemplate;


    @PostConstruct
    public void init(){
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(DrugIndex.class);
        if(!indexOperations.exists()){
            indexOperations.createWithMapping();
        }
    }

    /**
     * 分页查询
     * @param request 请求参数
     * @return
     */
    public PageResp<DrugIndex> pageQuery(DrugPageRequest request) {
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        if (StrUtil.isNotBlank(request.getKeyword())) {
            nativeQueryBuilder.withQuery(queryBuilder ->
                    queryBuilder.multiMatch(matchQueryBuilder ->
                            matchQueryBuilder.query(request.getKeyword())
                                    .fields(DrugHighlightFields.getAllFieldList())
                    ));
        }
        if (null != request.getPageSize() && null != request.getCurrentPage()) {
            nativeQueryBuilder.withPageable(PageRequest.of(request.getCurrentPage(), request.getPageSize())).build();
        }
        NativeQuery nativeQuery = nativeQueryBuilder.build();
        SearchHits<DrugIndex> searchHits = elasticsearchTemplate.search(nativeQuery, DrugIndex.class);
        List<DrugIndex> list = searchHits.stream().map(SearchHit::getContent).toList();

        return PageResp.of(request.getCurrentPage(), request.getPageSize(), list, (int)searchHits.getTotalHits());

    }

    public PageResp<DrugIndex> lightQuery(@Valid DrugPageRequest request) {
        HighlightQuery highlightQuery = buildHighlightQuery();
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query ->
                        query.multiMatch(multiMatch ->
                                multiMatch.query(request.getKeyword())
                                        .fields(DrugHighlightFields.getAllFieldList())))
                .withHighlightQuery(highlightQuery)
                .withPageable(PageRequest.of(request.getCurrentPage(), request.getPageSize()))
                .build();

        SearchHits<DrugIndex> searchResult = elasticsearchTemplate.search(nativeQuery, DrugIndex.class);

        ArrayList<DrugIndex> indices = new ArrayList<>();
        // 处理高亮结果
        searchResult.forEach(hit -> {
            DrugIndex content = hit.getContent();
            JSONObject obj = JSONUtil.parseObj(content);
            hit.getHighlightFields().forEach((field, highlights) -> {
                obj.set(field,highlights.getFirst());
            });
            DrugIndex bean = JSONUtil.toBean(obj, DrugIndex.class);
            indices.add(bean);
        });

        return PageResp.of(request.getCurrentPage(), request.getPageSize(), indices, (int)searchResult.getTotalHits());
    }


    private HighlightQuery buildHighlightQuery() {
        List<String> allFieldList = DrugHighlightFields.getAllFieldList();
        List<HighlightField> highlightFieldList = new ArrayList<>();
        allFieldList.forEach(field -> {
            highlightFieldList.add(new HighlightField(field));
        });
        Highlight highlight = new Highlight(highlightFieldList);
        return new HighlightQuery(highlight, DrugIndex.class);
    }
}
