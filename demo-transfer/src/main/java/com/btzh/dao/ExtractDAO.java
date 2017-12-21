package com.btzh.dao;

import java.util.List;
import java.util.Map;

/**
 * 出库
 * @author tanzhibo
 * @date 2017/9/25
 */
public interface ExtractDAO {

	/**
	 * 通过表名获取所有列名
	 * @param tableName
	 * @return
	 */
	List<String> listColumn(String tableName);

	/**
	 * 通过表名获取所有主键名称
	 * @param tableName
	 * @return
	 */
	List<String> listPK(String tableName);

	/**
	 * 获取所有记录变动数据表名
	 * @return
	 */
	List<String> listCdcTableName();

	/**
	 * 计算数据量
	 * @param tableName
	 * @return
	 */
	Integer count(String tableName);

	/**
	 * 分页查询数据
	 * @param tableName
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String, Object>> pageData(String tableName, int start, int end);

	/**
	 * 批量更新数据
	 * @param tableName
	 * @param list
	 */
	void updateCdcData(String tableName, List<Map<String, Object>> list);

	/**
	 * 删除数据
	 * @param tableName
	 */
	void deleteCdcData(String tableName);
}