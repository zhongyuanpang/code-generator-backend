package com.pzy.codegenerator.common.utils.dao;


import lombok.*;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DaoAccessParam {
    private String url;  // IP地址
    private String port; //端口号
    private String dataBase; //数据库名称
    private String username; // 用户名
    private String password; // 密码
    private String dataType; //数据库类型 目前支持取值mysql/oracle/sqlserver/postgresql

    public DaoAccessParam(Map<String, Object> params) {
        this.url = (String) params.get("url");
        this.port = (String) params.get("port");
        this.dataBase = (String) params.get("dataBase");
        this.username = (String) params.get("username");
        this.password = (String) params.get("password");
        this.dataType = (String) params.get("dataType");
    }

    public String selectSQL(String sqlServer, String mysql) {
        if (this.isMySQL()) {
            return mysql;
        } else if (this.isSQLServer()) {
            return sqlServer;
        }
        return "";
    }

    public boolean isMySQL() {
        return "MySQL".equals(this.dataType);
    }

    public boolean isSQLServer() {
        return "SQLServer".equals(this.dataType);
    }
}