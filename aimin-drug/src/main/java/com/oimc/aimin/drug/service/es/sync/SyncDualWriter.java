package com.oimc.aimin.drug.service.es.sync;

import com.oimc.aimin.common.core.exception.CommonErrorType;
import com.oimc.aimin.common.core.exception.CommonException;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.service.DrugEsOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("sync_dual_write")
@RequiredArgsConstructor
public class SyncDualWriter implements EsSyncService {

    private final ObjectConvertor objectConvertor;
    private final DrugEsOperation drugEsOperationService;
    private final DrugService drugService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(Drug drug) {
        try{
            CompletableFuture<Void> mysqlFuture = CompletableFuture.runAsync(() -> drugService.save(drug));
            CompletableFuture<Void> esFuture = CompletableFuture.runAsync(() -> {
                DrugIndex drugIndex = objectConvertor.drug2DrugIndex(drug);
                drugEsOperationService.save(drugIndex);
            });

            // 等待所有任务完成
            CompletableFuture.allOf(mysqlFuture, esFuture).join();
        }catch (Exception e){
            throw new CommonException(CommonErrorType.SYNC_FAILURE);
        }
    }

    @Override
    public void syncList(List<Drug> list) {
        list.forEach(this::sync);
    }
}
