package com.oimc.aimin.drug.canal;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.oimc.aimin.base.model.drug.index.DrugIndex;
import com.oimc.aimin.drug.model.entity.DrugCategory;
import com.oimc.aimin.drug.canal.config.CanalProperties;
import com.oimc.aimin.drug.model.entity.DrugImg;
import com.oimc.aimin.drug.service.DrugCategoryService;
import com.oimc.aimin.drug.service.DrugService;
import com.oimc.aimin.file.base.FileService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 *
 * @author 渣哥
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CanalReceiver {

    private final CanalConnector canalConnector;

    private final CanalProperties canalProperties;

    private final ElasticsearchTemplate elasticsearchTemplate;

    private long batchId;

    private final DrugCategoryService categoryService;

    private final DrugService drugService;
    
    private final FileService fileService;


    @PostConstruct
    public void connect() {
        canalConnector.connect();
        canalConnector.subscribe(canalProperties.getSubscribe());
        canalConnector.rollback();
    }

    @PreDestroy
    public void disConnect() {
        canalConnector.disconnect();
    }

    @Async
    @Scheduled(initialDelayString = "${canal.initialDelay:3000}", fixedDelayString = "${canal.fixedDelay:3000}")
    public void processData() {
        try {
            if (!canalConnector.checkValid()) {
                log.warn("=====>与Canal服务器的连接失效！！！重连，下个周期再检查数据变更");
                this.connect();
            } else {
                //获取batchSize条数据
                Message message = canalConnector.getWithoutAck(100,100L, TimeUnit.MILLISECONDS);
                batchId = message.getId();
                int size = message.getEntries().size();

                if (batchId > -1 || size > 0) {
                    for (CanalEntry.Entry entry : message.getEntries()) {
                        String tableName = entry.getHeader().getTableName();
                        if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                                || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND
                                || tableName.equals("ha_health_check")) {
                            continue;
                        }
                        log.info(entry.getEntryType().toString());
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        log.info("=====>数据变更详情：来自binglog[{}.{}], 数据源{}.{}, 变更类型{}",
                                entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                                entry.getHeader().getSchemaName(), tableName, eventType);

                        //处理数据
                        handleData(entry);
                    }

                    // 提交确认
                    canalConnector.ack(batchId);
                    log.info("=====>本批次[{}]Canal同步数据完成", batchId);
                }
            }
        } catch (Exception e) {
            log.error("=====>Canal同步数据失效，请检查：", e);
            if (batchId != -1) {
                canalConnector.rollback(batchId);
            }
        }
    }

    private void handleData(CanalEntry.Entry entry) throws Exception {
        CanalEntry.EntryType entryType = entry.getEntryType();
        String tableName = entry.getHeader().getTableName();
        if (entryType == CanalEntry.EntryType.ROWDATA) {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            CanalEntry.EventType eventType = rowChange.getEventType();
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                switch (eventType) {
                    case INSERT -> {
                        Map<String, Object> afterParam = rowData.getAfterColumnsList().stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                        if(tableName.equals("t_drug")){
                            insertDrug(afterParam);
                        }
                        if(tableName.equals("t_drug_img")){
                            insertDrugImg(afterParam);
                        }
                    }
                    case DELETE -> {
                        Map<String, Object> beforeParam = rowData.getBeforeColumnsList().stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                        log.info("删除: {}", beforeParam);
                    }
                    case UPDATE -> {
                        Map<String, Object> beforeParam = rowData.getBeforeColumnsList().stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                        Map<String, Object> afterParam = rowData.getAfterColumnsList().stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                        log.info("更新前==> {}", beforeParam);
                        log.info("更新后==> {}", afterParam);
                        updateDrug(afterParam);
                    }
                    default -> {
                        log.info("未匹配的类型,忽略: {}", eventType.name());
                    }
                }
            }
        }
    }

    private void insertDrug(Map<String, Object> afterParam){
        DrugIndex bean = BeanUtil.toBean(afterParam, DrugIndex.class);
        if(bean.getCategoryId() != null){
            DrugCategory category = categoryService.getByIdDeep(bean.getCategoryId());
            bean.setCategoryName(category.getName());
        }
        elasticsearchTemplate.save(bean);
        log.info("新增药品: {}", bean);
    }

    private void insertDrugImg(Map<String, Object> afterParam){
        DrugImg bean = BeanUtil.toBean(afterParam, DrugImg.class);
        Integer drugId = bean.getDrugId();
        if(drugId!= null){
            boolean exist = drugService.isExist(drugId);
            if(exist && bean.getIsMain()){
                String externalPath = fileService.externalPath(bean.getPath());
                DrugIndex drugIndex = new DrugIndex();
                drugIndex.setDrugId(drugId);
                drugIndex.setImgUrl(externalPath);
                elasticsearchTemplate.update(drugIndex);
                log.info("更新药品图片: {}", externalPath);
            }
        }
    }

    private void updateDrug(Map<String, Object> afterParam){
        DrugIndex bean = BeanUtil.toBean(afterParam, DrugIndex.class);
        if(bean.getDeleted()){
            elasticsearchTemplate.delete(bean);
        }else{
            elasticsearchTemplate.update(bean);
        }
    }

    // todo 时间需要转化
    private DrugIndex mapToIndex(Map<String, Object> afterParam){
        return null;
    }
}
