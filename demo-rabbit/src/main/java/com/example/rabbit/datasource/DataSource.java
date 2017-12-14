package com.example.rabbit.datasource;

import com.btzh.mis.core.dao.OracleBaseDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * @description  基础库
 * @author xuhandong
 * @time 2017/7/6
 */
public class DataSource<T> extends OracleBaseDaoImpl<T> {
    @Resource(name="jdbcTemplate")
    public void setDataSource(JdbcTemplate jdbcTemplate){
        super.jdbcTemplate= jdbcTemplate;
    }
}
