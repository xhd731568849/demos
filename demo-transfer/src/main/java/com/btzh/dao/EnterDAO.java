package com.btzh.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 入库
 * @author tanzhibo
 * @date 2017/9/25
 */
public interface EnterDAO {
    /**
     * 批量处理
     * @param sql
     * @param list
     * @param columns
     */
    void batch(String sql, List<JSONObject> list, List<String> columns);
}
