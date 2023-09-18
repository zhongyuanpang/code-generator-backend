package com.pzy.codegenerator.domain;

import lombok.Data;

@Data
public class TableInfo {

    // 字段名称
    private String COLUMN_NAME;

    // 字段注释
    private String COLUMN_COMMENT;

    // 字段类型
    private String DATA_TYPE;

    // 字段长度
    private String CHARACTER_MAXIMUM_LENGTH;

    // 是否为空
    private String IS_NULLABLE;

    // 列的默认值
    private String COLUMN_DEFAULT;

    // 列是否为主键或索引
    private String COLUMN_KEY;
}
