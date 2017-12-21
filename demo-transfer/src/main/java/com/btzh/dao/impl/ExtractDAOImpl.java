package com.btzh.dao.impl;

import com.btzh.consts.DatabaseInfo;
import com.btzh.dao.ExtractDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库
 * @author tanzhibo
 * @date 2017/9/25
 */
@Repository
public class ExtractDAOImpl implements ExtractDAO {

	@Resource(name = "extractJdbcTemplate")
	private JdbcTemplate extractJdbcTemplate;

	@Override
	public List<String> listColumn(String tableName) {
		return extractJdbcTemplate.query(DatabaseInfo.SELECT_FIELD_INFO_SQL, new Object[] { tableName.toUpperCase() }, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> resList = new ArrayList<>();
				while (rs.next()) {
					resList.add(rs.getObject("COLUMN_NAME").toString());
				}
				return resList;
			}
		});
	}

	@Override
	public List<String> listPK(String tableName) {
		return extractJdbcTemplate.query(DatabaseInfo.SELECT_PK_SQL, new Object[] { tableName.toUpperCase() }, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getObject("COLUMN_NAME").toString());
				}
				return list;
			}
		});
	}

	@Override
	public List<String> listCdcTableName() {
		return extractJdbcTemplate.query(DatabaseInfo.SELECT_CDC_TABLE_NAME_SQL, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getObject("TABLE_NAME").toString());
				}
				return list;
			}
		});
	}

	@Override
	public Integer count(String tableName) {
		return extractJdbcTemplate.query(DatabaseInfo.COUNT_SQL + tableName, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return Integer.valueOf(rs.getObject(1).toString());
				}
				return 0;
			}
		});
	}

	@Override
	public List<Map<String, Object>> pageData(String tableName, int start, int end) {
		StringBuilder sql = new StringBuilder("SELECT * FROM")
				.append(" (SELECT a.*, rownum rn, '0' bm_is_delete FROM (SELECT * FROM ").append(tableName)
				.append(" ORDER BY rsid$) A WHERE rownum <= ?)").append(" WHERE rn > ?");
		return extractJdbcTemplate.query(sql.toString(), new Object[] { end, start }, new ResultSetExtractor<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Map<String, Object>> list = new ArrayList<>();
				while (rs.next()) {
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					Map<String, Object> map = new HashMap<>(columnCount);
					for (int i = 1; i <= columnCount; i++) {
						// 字段名称
						String columnLabel = metaData.getColumnLabel(i);
						map.put(columnLabel, rs.getObject(columnLabel));
					}
					list.add(map);
				}
				return list;
			}
		});
	}

	@Override
	public void updateCdcData(String tableName, List<Map<String, Object>> list) {
		StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET send_type=1 WHERE ").append(DatabaseInfo.CDC_FILED_RSID$).append("=?");
		extractJdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, list.get(i).get(DatabaseInfo.CDC_FILED_RSID$));
			}
		});
	}

	@Override
	public void deleteCdcData(String tableName) {
		extractJdbcTemplate.update("DELETE FROM " + tableName + " WHERE send_type = 1");
	}
}