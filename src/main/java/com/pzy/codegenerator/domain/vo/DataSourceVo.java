package com.pzy.codegenerator.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceVo {
    private String key; // 给数据源的命名
    private String url;  // IP地址
    private String port; //端口号
    private String dataBase; //数据库名称
    private String username; // 用户名
    private String password; // 密码
    private String dataType; //数据库类型 目前支持取值mysql/oracle/sqlserver/postgresql
}
