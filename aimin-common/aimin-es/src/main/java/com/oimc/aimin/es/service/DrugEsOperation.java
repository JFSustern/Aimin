package com.oimc.aimin.es.service;

import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.index.DrugIndex;

import java.util.List;

public interface DrugEsOperation {

    Iterable<DrugIndex> batchSave (List<DrugIndex> Indexes);

    DrugIndex save(DrugIndex drugIndex);

    void deleteByIds(List<Integer> ids);

    List<DrugIndex> searchDrugsWithHighlight(DrugSearchRequest request);

    List<DrugIndex> fetchPage(DrugSearchRequest request);

}
