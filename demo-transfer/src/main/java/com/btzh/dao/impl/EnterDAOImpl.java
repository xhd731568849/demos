package com.btzh.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.btzh.dao.EnterDAO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 入库
 * @author tanzhibo
 * @date 2017/9/25
 */
@Repository
public class EnterDAOImpl implements EnterDAO {
    @Resource(name = "enterJdbcTemplate")
    private JdbcTemplate enterJdbcTemplate;

    @Override
    public void batch(String sql, List<JSONObject> list, List<String> columns) {
        enterJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return list.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JSONObject dataMap = list.get(i);
                int c = 1;
                for (String column : columns) {
                    ps.setObject(c, dataMap.get(column));
                    c++;
                }
            }
        });
    }
}