package com.oimc.aimin.es.repository;

import com.oimc.aimin.es.index.DrugIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface DrugIndexRepository extends ElasticsearchRepository<DrugIndex,Integer> {

}
