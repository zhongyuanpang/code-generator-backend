package com.pzy.codegenerator.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.pzy.codegenerator.common.utils.SpringContextUtils;
import com.pzy.codegenerator.domain.vo.DataSourceVo;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description //切换数据源的核心配置类
 * @Author pzy
 * @Date 2023/7/31 19:52
 **/
//涉及到数据源一定要加事务管理注解
@EnableTransactionManagement
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Autowired
    private DataSourceConfig dataSourceConfig;

    // 通过ThreadLocal线程隔离的优势线程存储线程，当前线程只能操作当前线程的局部变量
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    // 把已有的数据源封装在Map里
    private Map<Object, Object> dynamicTargetDataSources = new HashMap<>();

    //通过重写AbstractRoutingDataSource的内置函数，来通过当前连接的数据源的key，进行数据源的获取
    @Override
    protected Object determineCurrentLookupKey() {
        if (StringUtils.isEmpty(getDataSource())) {
            return "default";
        }
        return getDataSource();
    }

    // 设置默认数据源（必须要有，否则无法启动）
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    // 通过设置数据源
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }

    // 切换数据源,更改ThreadLocal中的局部变量
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源(每次切换数据源的时候都应先移除已有数据源)
    public static void clearDataSource() {
        contextHolder.remove();
    }

    //创建一个新的数据源连接，并且设置此数据源为我们要用的数据源
    public boolean createDataSource(DataSourceVo dataSourceVo) throws NoSuchFieldException, IllegalAccessException {
        // 获取配置在Yaml文件中的所有数据源信息
        Map<String, Object> dataBaseConfig = dataSourceConfig.getDataBaseConfig();
        // 根据数据库类型获取数据源信息
        Map<String, String> dataConfig = (Map<String, String>) dataBaseConfig.get(dataSourceVo.getDataType());
        if (dataConfig == null) {
            // 不支持此类数据源
            throw new RuntimeException("不支持此数据源！");
        }
        String driveName = dataConfig.get("driverClassName");
        // url  替换其中的占位符
        String url = dataConfig.get("url").replaceAll("\\{IP}", dataSourceVo.getUrl())
                .replaceAll("\\{port}", dataSourceVo.getPort())
                .replaceAll("\\{database}", dataSourceVo.getDataName());
        // 测试连接
        testConnection(driveName, url, dataSourceVo.getUsername(), dataSourceVo.getPassword());

        // 通过Druid数据库连接池连接数据库
        DruidDataSource dataSource = new DruidDataSource();
        //接收前端传递的参数并且注入进去
        dataSource.setName(dataSourceVo.getDataName());
        dataSource.setUrl(url);
        dataSource.setUsername(dataSourceVo.getUsername());
        dataSource.setPassword(dataSourceVo.getPassword());
        dataSource.setDriverClassName(driveName);
        // 设置最大连接等待时间
        dataSource.setMaxWait(4000);

        // 数据源初始化
        try {
            dataSource.init();
        } catch (SQLException e) {
            // 创建失败则抛出异常
            throw new RuntimeException();
        }
        //获取当前数据源的键值对存入Map
        this.dynamicTargetDataSources.put(dataSourceVo.getKey(), dataSource);
        // 设置数据源
        this.setTargetDataSources(this.dynamicTargetDataSources);
        // 解析数据源
        super.afterPropertiesSet();
        // 切换数据源
        setDataSource(dataSourceVo.getKey());
        /**
         ** 修改mybatis的数据源
         * ！！！重要，不修改mybatis的数据源的话，
         * 即使切换了数据源之后还是会出现默认数据源的情况
         */
        SqlSessionFactory SqlSessionFactory = (SqlSessionFactory) SpringContextUtils.getBean(SqlSessionFactory.class);
        Environment environment = SqlSessionFactory.getConfiguration().getEnvironment();
        Field dataSourceField = environment.getClass().getDeclaredField("dataSource");
        //跳过检验
        dataSourceField.setAccessible(true);
        //修改mybatis的数据源
        dataSourceField.set(environment, dataSource);
        //修改完成后所有线程使用此数据源
        return true;
    }

    // 测试数据源连接的方法
    public void testConnection(String driveClass, String url, String username, String password) {
        try {
            Class.forName(driveClass);
            DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
