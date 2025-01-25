package com.oimc.aimin.drug.service.es.sync;

import com.oimc.aimin.drug.entity.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("async_dual_write")
@RequiredArgsConstructor
public class AsyncDualWriter implements EsSyncService{

    @Override
    public void sync(Drug drug) {

    }

    @Override
    public void syncList(List<Drug> list) {

    }
}
