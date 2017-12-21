package com.btzh.service.db.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.btzh.consts.DatabaseInfo;
import com.btzh.dao.EnterDAO;
import com.btzh.service.SqlProcessor;
import com.btzh.transfer.entity.Text;
/**
 * sql处理
 * @author wanglidong
 * @date 2017/9/25
 */
@Service("sqlProcessor")
public class SqlProcessorImpl implements SqlProcessor {

	private final static Logger logger = LoggerFactory.getLogger(SqlProcessorImpl.class);

	@Autowired
	private EnterDAO enterDAO;

	@Override
	@SuppressWarnings("unchecked")
	public void process(Text sql) {
		logger.info("入库 start");
		long start = System.currentTimeMillis();

		JSONObject data = JSON.parseObject(sql.getText(), JSONObject.class);
		List<String> columns = (List<String>) data.get(DatabaseInfo.DML_COLUMNS);
		List<String> keys = (List<String>) data.get(DatabaseInfo.DML_KEYS);
		List<JSONObject> list = (List<JSONObject>) data.get(DatabaseInfo.CDC_DATA);
		for (int i = 0; i < list.size(); i++) {
			JSONObject map = list.get(i);
			if (DatabaseInfo.DML_INSERT.equals(map.get(DatabaseInfo.DML_TYPE))) {
				enterDAO.batch(map.get(DatabaseInfo.DML_SQL).toString(), (List<JSONObject>) map.get(DatabaseInfo.DML_DATA), columns);
			} else if (DatabaseInfo.DML_UPDATE.equals(map.get(DatabaseInfo.DML_TYPE))) {
				enterDAO.batch(map.get(DatabaseInfo.DML_SQL).toString(), (List<JSONObject>) map.get(DatabaseInfo.DML_DATA), columns);
			} else if (DatabaseInfo.DML_DELETE.equals(map.get(DatabaseInfo.DML_TYPE))) {
				enterDAO.batch(map.get(DatabaseInfo.DML_SQL).toString(), (List<JSONObject>) map.get(DatabaseInfo.DML_DATA), keys);
			}
		}

		long end = System.currentTimeMillis();
		logger.info("入库 end" + "，time:" + (end - start) / 1000.0);
	}
}