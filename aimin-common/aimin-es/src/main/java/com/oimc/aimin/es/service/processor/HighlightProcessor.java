package com.oimc.aimin.es.service.processor;

import com.oimc.aimin.es.index.DrugIndex;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
/**
 * 高亮处理器类
 * <p>
 * 该类负责处理 Elasticsearch 搜索结果中的高亮字段，将高亮结果提取并返回。
 * 通过将每个字段的高亮处理逻辑封装为独立的函数，并使用 Map 进行管理，避免了大量的 if-else 语句，
 * 使代码更加简洁、可维护性更强。
 * </p>
 * <p>
 * 该类是一个 Spring 组件（@Component），由 Spring 容器管理，可以在其他服务类中直接注入使用。
 * </p>
 */
@Service
public class HighlightProcessor {
    /**
     * 高亮处理逻辑的 Map
     * <p>
     * Key 为字段名称（如 "name"、"description"），
     * Value 为对应字段的高亮处理函数。
     * 每个函数接收一个 SearchHit 对象，返回该字段的高亮结果（如果有）。
     * </p>
     */
    private final Map<String, Function<SearchHit<DrugIndex>, Optional<String>>> highlightHandlers = new HashMap<>();



    /**
     * 获取高亮结果
     * <p>
     * 遍历搜索结果中的所有高亮字段，调用对应的高亮处理函数，返回第一个匹配的高亮结果。
     * </p>
     *
     * @param hit Elasticsearch 的搜索结果对象，包含文档内容和高亮字段
     * @return 高亮结果字符串，如果没有高亮字段则返回 null
     */
    public String getHighlightResult(SearchHit<DrugIndex> hit) {
        // 遍历所有高亮字段
        for (String fieldName : hit.getHighlightFields().keySet()) {
            // 获取对应字段的高亮处理函数
            Function<SearchHit<DrugIndex>, Optional<String>> handler = highlightHandlers.get(fieldName);
            if (handler != null) {
                // 调用高亮处理函数，获取高亮结果
                Optional<String> highlight = handler.apply(hit);
                if (highlight.isPresent()) {
                    // 如果高亮结果存在，则返回
                    return highlight.get();
                }
            }
        }
        // 如果没有高亮字段，则返回 null
        return null;
    }

    /**
     * 根据字段名称获取高亮结果
     * <p>
     * 从 SearchHit 对象中提取指定字段的高亮结果。
     * </p>
     *
     * @param hit       Elasticsearch 的搜索结果对象，包含文档内容和高亮字段
     * @param fieldName 字段名称（如 "name"、"description"）
     * @return 高亮结果的 Optional 对象，如果字段没有高亮则返回 Optional.empty()
     */
    private Optional<String> getHighlightField(SearchHit<DrugIndex> hit, String fieldName) {
        // 检查字段是否有高亮结果
        if (hit.getHighlightFields().containsKey(fieldName)) {
            // 返回字段的第一个高亮结果
            return Optional.of(hit.getHighlightFields().get(fieldName).get(0));
        }
        // 如果字段没有高亮结果，则返回 Optional.empty()
        return Optional.empty();
    }
}
