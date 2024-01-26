package com.pzy.codegenerator.common.utils.dao;


import com.pzy.codegenerator.common.utils.Result;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DaoUtil {

    public static List<Map<String, Object>> executeSelect(Connection connection, String table) throws SQLException {
        String sql = "SELECT 1 FROM " + table;

        log.info("START----------------------------------------------------------------------------------------------");
        log.info("SQL: {}", sql);

        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            //执行 sql 语句的对象
            ResultSet resultSet = prepareStatement.executeQuery("show full columns from " + table);

            List<Map<String, Object>> list = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> map = new LinkedHashMap();

                map.put("Field", resultSet.getString("Field"));

                String rawType = resultSet.getString("Type");
                String[] array = rawType.split("[()]");

                map.put("Type", rawType);

                String length = "";
                try {
                    length = array[1];
                } catch (Exception e) {
                }
                map.put("Length", length);

                map.put("Null", resultSet.getString("Null"));
                map.put("Key", resultSet.getString("Key"));
                map.put("Default", resultSet.getString("Default"));
                map.put("Extra", resultSet.getString("Extra"));
                map.put("Comment", resultSet.getString("Comment"));
                list.add(map);
            }
            log.info("RESULT: {}", list);
            log.info("END------------------------------------------------------------------------------------------------\n\n\n");

            return list;
        }
    }

    public static List<Map<String, Object>> executeSelect(Connection connection, String sql, Object[] sqlParams) throws SQLException {

        log.info("START----------------------------------------------------------------------------------------------");
        log.info("SQL: {}", sql);

        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= sqlParams.length; i++) {

                Object param = sqlParams[i - 1];

                log.info("param-" + i + ": {}", param);

                prepareStatement.setObject(i, param);
            }

            //执行 sql 语句的对象
            ResultSet resultSet = prepareStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();

            List<Map<String, Object>> list = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> map = new LinkedHashMap();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = metaData.getColumnName(i + 1);
                    Object cols_value = resultSet.getObject(cols_name);
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
            log.info("RESULT: {}", list);
            log.info("END------------------------------------------------------------------------------------------------\n\n\n");

            return list;
        }
    }

    public static int executeAlter(Connection connection, String sql, Object[] sqlParams) throws RuntimeException, SQLException {

        log.info("START----------------------------------------------------------------------------------------------");
        log.info("SQL: {}", sql);

        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= sqlParams.length; i++) {

                Object param = sqlParams[i - 1];

                log.info("param-" + i + ": {}", param);

                prepareStatement.setObject(i, param);
            }

            int result = prepareStatement.executeUpdate();

            log.info("RESULT: {}", result);
            log.info("END------------------------------------------------------------------------------------------------\n\n\n");
            //执行 sql 语句
            return result;
        }
    }

    public static int[] executeAlterBatch(Connection connection, String sql, Object[][] sqlParamsArray) throws SQLException {

        log.info("START----------------------------------------------------------------------------------------------");
        log.info("SQL: {}", sql);

        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= sqlParamsArray.length; i++) {

                Object[] sqlParams = sqlParamsArray[i - 1];

                for (int j = 1; j <= sqlParams.length; j++) {

                    Object param = sqlParams[j - 1];

                    log.info("param-" + i + "-" + j + ": {}", param);

                    prepareStatement.setObject(j, param);
                }
                prepareStatement.addBatch();
            }


            // 执行Batch
            int[] result = prepareStatement.executeBatch();

            // 清空Batch
            prepareStatement.clearBatch();

            log.info("RESULT: {}", result);

            log.info("END------------------------------------------------------------------------------------------------\n\n\n");
            return result;
        }
    }


    // TODO java 9 可以进一步优化流的关闭，不再需要使用传统方式关闭流
    public static Result connect(DaoAccessParam daoAccessParam, SqlHandler sqlHandler) throws Exception {
        String url = DaoUtil.processType(daoAccessParam);

        Connection connection = DriverManager.getConnection(url, daoAccessParam.getUsername(), daoAccessParam.getPassword());
        try {

            connection.setAutoCommit(false);
            Result result = sqlHandler.method(connection, daoAccessParam);
            connection.commit();

            return result;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    private static String processType(DaoAccessParam daoAccessParam) throws ClassNotFoundException {
        String url = "";
        if (daoAccessParam.isSQLServer()) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            url = "jdbc:sqlserver://" + daoAccessParam.getUrl() + ":" + daoAccessParam.getPort() + ";databaseName=" + daoAccessParam.getDataBase() + ";integratedSecurity=false";
        } else if (daoAccessParam.isMySQL()) {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + daoAccessParam.getUrl() + ":" + daoAccessParam.getPort() + "/" + daoAccessParam.getDataBase() + "?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";
        }
        log.info("url: " + url);
        return url;
    }

}
