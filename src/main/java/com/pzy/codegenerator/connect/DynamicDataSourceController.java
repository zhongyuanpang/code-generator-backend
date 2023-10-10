package com.pzy.codegenerator.connect;

import com.pzy.codegenerator.common.config.DynamicDataSource;
import com.pzy.codegenerator.common.enums.ResultCode;
import com.pzy.codegenerator.common.utils.Result;
import com.pzy.codegenerator.domain.vo.DataSourceVo;
import com.pzy.codegenerator.connect.mapper.CommonMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping("datasource")
@CrossOrigin
@Slf4j
public class DynamicDataSourceController {

    @Resource
    private DynamicDataSource dynamicDataSource;

    @Resource
    private CommonMapper commonMapper;

    /**
     * @Description 建立链接
     * @Author pzy
     * @Date 2023/8/3 20:08
     **/
    @PostMapping("/connect")
    public Result connect(@RequestBody DataSourceVo dataSourceVo) throws Exception {
        //切换数据源之前先清空
        DynamicDataSource.clearDataSource();
        //切换数据源
        dynamicDataSource.createDataSource(dataSourceVo);

        log.info("当前数据源:{}", dynamicDataSource.getConnection());
        return new Result(ResultCode.SUCCESS, "当前数据源:" + dynamicDataSource.getConnection());
    }

    /**
     * @Description 获取所有数据表
     * @Author pzy
     * @Date 2023/8/3 20:08
     **/
    @GetMapping("/getAllTable")
    public Result getAllTable(@RequestParam("dataBase") String dataBase) {
        List<Map<String, Object>> allTable = commonMapper.getAllTable(dataBase);
        return new Result(ResultCode.SUCCESS, allTable);
    }

    /**
     * @Description 根据表名获取列信息
     * @Author pzy
     * @Date 2023/8/4 15:40
     **/
    @PostMapping("/getTableColumnInfo")
    public Result getTableColumnInfo(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> tableColumnInfo = commonMapper.getTableColumnInfo(map);
        return new Result(ResultCode.SUCCESS, tableColumnInfo);
    }

    /**
     * @Description 根据数据库名查询表数量
     * @Author pzy
     * @Date 2023/9/20 11:40
     **/
    @GetMapping("/getTableCount")
    public Result getTableCount(@RequestParam("dataBase") String dataBase){
        Integer count = commonMapper.getTableCount(dataBase);
        return new Result(ResultCode.SUCCESS, count);
    }

}
