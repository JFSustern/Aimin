package com.oimc.aimin.drug.service.es.sync;

import com.oimc.aimin.drug.entity.Drug;

import java.util.List;

public interface EsSyncService {

    void sync(Drug drug);

    void syncList(List<Drug> list);


}
