package com.pzy.codegenerator.connect;

import com.pzy.codegenerator.common.enums.ResultCode;
import com.pzy.codegenerator.common.utils.Result;
import com.pzy.codegenerator.common.utils.dao.DaoAccessParam;
import com.pzy.codegenerator.common.utils.dao.DaoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping("datasource")
@CrossOrigin
@Slf4j
public class DynamicDataSourceController {

    /**
     * @Description 建立链接
     * @Author pzy
     * @Date 2023/8/3 20:08
     **/
    @PostMapping("/connectionTest")
    public Result connect(@RequestBody DaoAccessParam params) throws Exception {
        return DaoUtil.connect(params, (Connection connection, DaoAccessParam daoAccessParam) -> {
            // 执行 SQL
            DaoUtil.executeSelect(connection, "SELECT 1", new Object[0]);
            return new Result(ResultCode.SUCCESS);
        });
    }


    @ApiOperation("获取当前数据库中的所有表名和其对应的注释")
    @PostMapping("/getTables")
    public Result getTables(@RequestBody DaoAccessParam params) throws Exception {
        return DaoUtil.connect(params, (Connection connection, DaoAccessParam daoAccessParam) -> {

            // 1.选择 SQL 语句
            String sql = daoAccessParam.selectSQL(
                    "",
                    "SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.tables WHERE TABLE_SCHEMA = ? ORDER BY TABLE_NAME DESC;");

            // 2.封装 SQL 参数
            Object[] sqlParams = new Object[1];
            sqlParams[0] = params.getDataBase();

            // 3.执行 SQL
            List<Map<String, Object>> records = DaoUtil.executeSelect(connection, sql, sqlParams);

            return new Result(ResultCode.SUCCESS, records);
        });
    }

    @ApiOperation("获取当前表的所有字段信息")
    @PostMapping("/getColumns")
    public Result getColumns(@RequestBody Map<String, Object> params) throws Exception {
        return DaoUtil.connect(new DaoAccessParam(params), (Connection connection, DaoAccessParam daoAccessParam) -> {

            // 执行 SQL
            List<Map<String, Object>> records = DaoUtil.executeSelect(connection, (String) params.get("table"));

            return new Result(ResultCode.SUCCESS, records);
        });
    }

}
