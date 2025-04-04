package com.oimc.aimin.search.drug.constants;

import java.util.Arrays;
import java.util.List;

public class DrugHighlightFields {

    /**
     * 药品名称字段
     */
    public static final String NAME = "name";

    /**
     * 药品描述字段
     */
    public static final String DESCRIPTION = "description";

    /**
     * 药品通用名字段
     */
    public static final String GENERIC_NAME = "genericName";

    /**
     * 品牌名字段
     */
    public static final String BRAND = "brand";

    /**
     * 生产厂家字段
     */
    public static final String MANUFACTURER = "manufacturer";

    /**
     * 剂型字段
     */
    public static final String DOSAGE_FORM = "dosageForm";

    /**
     * 获取所有高亮字段
     *
     * @return 高亮字段数组
     */
    public static String[] getAllFieldArray() {
        return new String[]{NAME, DESCRIPTION, GENERIC_NAME, BRAND, MANUFACTURER, DOSAGE_FORM};
    }
    public static List<String> getAllFieldList() {
        return Arrays.asList(getAllFieldArray());
    }
}
