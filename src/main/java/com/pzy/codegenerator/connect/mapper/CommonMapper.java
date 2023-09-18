package com.pzy.codegenerator.connect.mapper;

import com.pzy.codegenerator.domain.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {

    /**
     * @Description 获取所有数据表信息
     * @Author pzy
     * @Date 2023/8/4 10:10
     **/
    List<Map<String, Object>> getAllTable(@Param("query") String query);

    /**
     * @Description 根据表名获取列信息
     * @Author pzy
     * @Date 2023/8/4 10:10
     **/
    List<Map<String, Object>> getTableColumnInfo(Map<String, Object> map);
}
