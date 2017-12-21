package com.btzh.job;

import com.alibaba.fastjson.JSON;
import com.btzh.config.Config;
import com.btzh.consts.BusinessType;
import com.btzh.consts.DatabaseInfo;
import com.btzh.dao.ExtractDAO;
import com.btzh.transfer.Client;
import com.btzh.transfer.entity.EncryptedText;
import com.btzh.transfer.entity.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 数据发送定时任务
 * @author lilin
 * @date 2017/9/25
 */
@Service("dbDataSend")
public class DbDataSend {

    private final static Logger logger = LoggerFactory.getLogger(DbDataSend.class);

    @Autowired
    private ExtractDAO extractDAO;
    @Autowired
    private Config config;
    private final static String BM_IS_DELETE = "1";

    private static ExecutorService executor = null;
    private static ExecutorService executorSend  = null;

    public void send() {
        logger.info("dbDataSend start");
        long start = System.currentTimeMillis();
        if (null == executor) {
            int businessThreadPoolSize = config.getBusinessThreadPoolSize();
            ThreadFactory namedThreadFactory = (Runnable r)-> new Thread(r, "page_thread_pool_" + r.hashCode());
            executor = new ThreadPoolExecutor(businessThreadPoolSize, businessThreadPoolSize,
                    3, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        }

        if (null == executorSend) {
            int businessThreadPoolSize = config.getBusinessThreadPoolSize();
            ThreadFactory namedThreadFactory = (Runnable r)-> new Thread(r, "send_thread_pool_" + r.hashCode());
            executorSend = new ThreadPoolExecutor(businessThreadPoolSize, businessThreadPoolSize,
                    3, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        }

        //获取有变动数据的表
        List<String> tableList = getTableNameExistChangeData();
        if (tableList == null || tableList.size() == 0) {
            logger.info("dbDataSend end，tables:0，send time:0");
            return;
        }
        for (String table : tableList) {
            //删除已发送的数据
            extractDAO.deleteCdcData(table);
        }

        try {
            CountDownLatch latch = new CountDownLatch(tableList.size());
            for (String table : tableList) {
                executor.execute(() -> batchProcessing(latch, table));
            }
            latch.await();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        long end = System.currentTimeMillis();
        logger.info("dbDataSend end，tables:" + tableList.size() + "，send time:" + (end - start) / 1000.0);
    }

    private void batchProcessing(CountDownLatch latch, String table) {
        logger.info("batchProcessing start");
        Client client = new Client();
        //分页处理发送数据
        int total = extractDAO.count(table);
        if (total == 0) {
            latch.countDown();
            return;
        }

        Integer pageSize = config.getPageSize();
        int pageIndex = 0;
        int pageCount = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
        List<String> keys = extractDAO.listPK(table.substring(4));
        if(keys.size() == 0){
            logger.warn(table.substring(4) + "没有设置主键，所有交换表必须有主键！");
            latch.countDown();
            return;
        }
        List<String> columns = extractDAO.listColumn(table.substring(4));
        CountDownLatch latchSend = new CountDownLatch(pageCount);
        try {
            do {
                int pageStart = pageIndex * pageSize;
                int pageEnd = pageStart + pageSize;
                if (pageEnd > total) {
                    pageEnd = total;
                }
                List<Map<String, Object>> list = extractDAO.pageData(table, pageStart, pageEnd);
                executorSend.execute(() -> nettySendData(latchSend, client, table, list, columns, keys));
                pageIndex++;
            } while (pageIndex < pageCount);
            latchSend.await();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        latch.countDown();
        logger.info("batchProcessing end");
    }

    private void nettySendData(CountDownLatch latchSend, Client client, String table, List<Map<String, Object>> list, List<String> columns, List<String> keys) {
        logger.info("nettySendData start");
        HashMap<String, List<?>> sendData = sendData(table, list, columns, keys);
        //密文传输
        if (config.getEncrypted()) {
            client.send(new EncryptedText(BusinessType.SQL, JSON.toJSONString(sendData)));
        } else {
            client.send(new Text(BusinessType.SQL, JSON.toJSONString(sendData)));
        }

        logger.info("updateCdcData start");
        //更新发送标志位
        extractDAO.updateCdcData(table, list);
        logger.info("updateCdcData end");
        latchSend.countDown();
        logger.info("nettySendData end");
    }

    private List<String> getTableNameExistChangeData() {
        List<String> tableNameList = new ArrayList<>();

        //拿到数据库中所有变动表表名的list
        List<String> changeTableNames = extractDAO.listCdcTableName();
        if (changeTableNames == null || changeTableNames.size() == 0) {
            return tableNameList;
        }
        for (String tableName : changeTableNames) {
            if (extractDAO.count(tableName) > 0) {
                tableNameList.add(tableName);
            }
        }
        return tableNameList;
    }

    private HashMap<String, List<?>> sendData(String tableName, List<Map<String, Object>> list, List<String> columns, List<String> keys) {
        for (int j = list.size() - 1; j >= 0; j--) {
            Map<String, Object> map = list.get(j);
            if (BM_IS_DELETE.equals(map.get(DatabaseInfo.CDC_FILED_BM_IS_DELETE).toString())) {
                continue;
            }
            for (Map<String, Object> map1 : list) {
                if (BM_IS_DELETE.equals(map1.get(DatabaseInfo.CDC_FILED_BM_IS_DELETE).toString())) {
                    continue;
                }
                boolean isSameRecord = true;
                for (String key : keys) {
                    if (!map.get(key).equals(map1.get(key))) {
                        isSameRecord = false;
                    }
                }
                if (isSameRecord && Integer.valueOf(map.get(DatabaseInfo.CDC_FILED_RSID$).toString()) > Integer.valueOf(map1.get(DatabaseInfo.CDC_FILED_RSID$).toString())) {
                    map1.put(DatabaseInfo.CDC_FILED_BM_IS_DELETE, "1");
                }
            }
        }

        List<Map<String, Object>> insertData = new ArrayList<>();
        List<Map<String, Object>> updateData = new ArrayList<>();
        List<Map<String, Object>> deleteData = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (BM_IS_DELETE.equals(map.get(DatabaseInfo.CDC_FILED_BM_IS_DELETE).toString())) {
                continue;
            }

            if ("I ".equals(map.get(DatabaseInfo.CDC_FILED_OPERATION$).toString())) {
                insertData.add(map);
            } else if ("UN".equals(map.get(DatabaseInfo.CDC_FILED_OPERATION$).toString())) {
                updateData.add(map);
            } else if ("D ".equals(map.get(DatabaseInfo.CDC_FILED_OPERATION$).toString())) {
                deleteData.add(map);
            }
        }

        List<HashMap<String, Object>> cdcData = new ArrayList<>();
        if (insertData.size() > 0) {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put(DatabaseInfo.DML_TYPE, DatabaseInfo.DML_INSERT);
            map.put(DatabaseInfo.DML_SQL, getSql(DatabaseInfo.DML_UPDATE, tableName.substring(4), columns, keys));
            map.put(DatabaseInfo.DML_DATA, insertData);
            cdcData.add(map);
        }
        if (updateData.size() > 0) {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put(DatabaseInfo.DML_TYPE, DatabaseInfo.DML_UPDATE);
            map.put(DatabaseInfo.DML_SQL, getSql(DatabaseInfo.DML_UPDATE, tableName.substring(4), columns, keys));
            map.put(DatabaseInfo.DML_DATA, updateData);
            cdcData.add(map);
        }
        if (deleteData.size() > 0) {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put(DatabaseInfo.DML_TYPE, DatabaseInfo.DML_DELETE);
            map.put(DatabaseInfo.DML_SQL, getSql(DatabaseInfo.DML_DELETE, tableName.substring(4), columns, keys));
            map.put(DatabaseInfo.DML_DATA, deleteData);
            cdcData.add(map);
        }

        HashMap<String, List<?>> changeData = new HashMap<>(3);
        changeData.put(DatabaseInfo.DML_COLUMNS, columns);
        changeData.put(DatabaseInfo.DML_KEYS, keys);
        changeData.put(DatabaseInfo.CDC_DATA, cdcData);
        return changeData;
    }

    private String getSql(String dmlType, String tableName, List<String> columns, List<String> keys) {
        if (DatabaseInfo.DML_INSERT.equals(dmlType)) {
            StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            StringBuilder insertSqlValues = new StringBuilder(" VALUES (");
            for (String column : columns) {
                insertSql.append(column).append(",");
                insertSqlValues.append("?,");
            }
            insertSqlValues.deleteCharAt(insertSqlValues.lastIndexOf(",")).append(") ");
            insertSql.deleteCharAt(insertSql.lastIndexOf(",")).append(") ").append(insertSqlValues);
            return insertSql.toString();
        }

        if (DatabaseInfo.DML_UPDATE.equals(dmlType)) {
            StringBuilder mergeIntoSql = new StringBuilder("MERGE INTO ").append(tableName).append(" T1 ");
            StringBuilder using = new StringBuilder("USING (SELECT ");
            StringBuilder on = new StringBuilder("ON ( ");
            StringBuilder update = new StringBuilder("UPDATE SET ");
            StringBuilder insert = new StringBuilder("INSERT (");
            StringBuilder values = new StringBuilder("VALUES(");

            for (String column : columns) {
                using.append("? AS ").append(column).append(",");
                if (!keys.contains(column)) {
                    update.append("T1.").append(column).append("=T2.").append(column).append(",");
                }
                insert.append(column).append(",");
                values.append("T2.").append(column).append(",");
            }
            for (String key : keys) {
                on.append("T1.").append(key).append("=T2.").append(key).append(" AND ");
            }

            using.deleteCharAt(using.lastIndexOf(",")).append(" FROM dual) T2 ");
            on.delete(on.lastIndexOf(" AND "), on.length()).append(") ");
            update.deleteCharAt(update.lastIndexOf(",")).append(" ");
            insert.deleteCharAt(insert.lastIndexOf(",")).append(") ");
            values.deleteCharAt(values.lastIndexOf(",")).append(") ");
            mergeIntoSql.append(using).append(on).append("WHEN MATCHED THEN ").append(update).append("WHEN NOT MATCHED THEN ").append(insert).append(values);
            return mergeIntoSql.toString();
        }

        if (DatabaseInfo.DML_DELETE.equals(dmlType)) {
            StringBuilder deleteSql = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE 1=1");
            for (String key : keys) {
                deleteSql.append(" AND ").append(key).append("=?");
            }
            return deleteSql.toString();
        }

        return "";
    }
}