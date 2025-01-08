package com.oimc.aimin.es.service;

import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.index.DrugIndex;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DrugEsOperationService {

    Iterable<DrugIndex> saveAll(List<DrugIndex> Indexes);

    void deleteByIds(List<Integer> ids);

    List<DrugIndex> searchDrugsWithHighlight(DrugSearchRequest request);

}
