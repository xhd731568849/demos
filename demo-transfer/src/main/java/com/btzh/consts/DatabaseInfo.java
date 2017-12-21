package com.btzh.consts;

/**
 * 数据常量
 * @author zhangg
 * @date 2017/10/22 15:03.
 */
public class DatabaseInfo {
	public static String SELECT_FIELD_INFO_SQL = "SELECT column_name FROM user_tab_columns WHERE table_name =?";
	public static String SELECT_PK_SQL = "SELECT col.column_name FROM user_constraints con,user_cons_columns col WHERE con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name=?";
	public static String SELECT_CDC_TABLE_NAME_SQL = "SELECT table_name FROM user_tables WHERE table_name LIKE 'CDC!_%' ESCAPE '!'";
	public static String COUNT_SQL = "SELECT COUNT(*) total FROM ";

	public static String CDC_FILED_BM_IS_DELETE = "BM_IS_DELETE";
	public static String CDC_FILED_OPERATION$ = "OPERATION$";
	public static String CDC_FILED_RSID$ = "RSID$";
	public static String CDC_DATA = "cdc_data";
	public static String DML_TYPE = "dml_type";
	public static String DML_SQL = "dml_sql";
	public static String DML_DATA = "dml_data";
	public static String DML_COLUMNS = "dml_columns";
	public static String DML_KEYS = "dml_keys";
	public static String DML_INSERT = "dml_insert";
	public static String DML_UPDATE = "dml_update";
	public static String DML_DELETE = "dml_delete";
}