package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oimc.aimin.drug.controller.vo.DrugVO;
import com.oimc.aimin.drug.dto.DrugSearchDto;
import com.oimc.aimin.drug.entity.Drug;
import com.oimc.aimin.drug.entity.DrugCategories;
import com.oimc.aimin.drug.mapper.DrugMapper;
import com.oimc.aimin.drug.service.es.sync.EsSyncService;
import com.oimc.aimin.drug.utils.ObjectConvertor;
import com.oimc.aimin.es.dto.DrugSearchRequest;
import com.oimc.aimin.es.index.DrugIndex;
import com.oimc.aimin.es.service.DrugEsOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
@Service
@AllArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    private final DrugMapper drugsMapper;
    @Qualifier("async_dual_write")
    private final EsSyncService esSyncService;
    private final DrugEsOperation drugEsOperation;
    private final ObjectConvertor objectConvertor;




    @Override
    public List<DrugVO> pageList(DrugSearchRequest request) {
        List<DrugIndex> drugIndices = drugEsOperation.fetchPage(request);
        return objectConvertor.drugIndexList2DrugVoList(drugIndices);
    }

    /**
     * 开启事务管理，确保在方法执行过程中如果发生异常，MySQL 的操作会回滚。
     * 使用 CompletableFuture.runAsync 创建两个异步任务：
     * mysqlFuture：将数据保存到 MySQL。
     * esFuture：将数据同步到 Elasticsearch。
     * 使用 CompletableFuture.allOf(mysqlFuture, esFuture).join() 等待所有任务完成。
     * 如果任何一个任务失败，join() 会抛出异常，触发事务回滚。
     *
     *
     * 劣势：事务仅适用于 MySQL 的操作，Elasticsearch 的操作在事务外执行。
     * 1. 如果 Elasticsearch 同步失败，MySQL 的操作会回滚，但 Elasticsearch 中可能已经写入部分数据。
     * 2. 如果 Elasticsearch 写入较慢，会影响整体性能。
     * 3. 扩展性差
     * @param drug
     */
    @Override
    public void saveAndSyncToES(Drug drug) {
        esSyncService.sync(drug);
    }

    @Override
    public List<DrugVO> searchWithHighlight(DrugSearchRequest request) {
        List<DrugIndex> drugIndices = drugEsOperation.searchDrugsWithHighlight(request);
        return objectConvertor.drugIndexList2DrugVoList(drugIndices);
    }

    @Override
    public List<Drug> selectAll() {
        MPJLambdaWrapper<Drug> wrapper = new MPJLambdaWrapper<Drug>()
                .selectAll(Drug.class)
                .selectAssociation("c", DrugCategories.class, Drug::getCategories)
                .leftJoin(DrugCategories.class, "c",DrugCategories::getCategoryId, Drug::getCategoryId);
        return drugsMapper.selectJoinList(wrapper);
    }

    @Override
    public void syncAllData2Es () {
        List<Drug> drugs = selectAll();
        List<DrugIndex> drugIndices = objectConvertor.drugList2IndexList(drugs);
        drugEsOperation.batchSave(drugIndices);
    }

}
