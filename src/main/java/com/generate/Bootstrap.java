package com.generate;


import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @author xhd
 * @date 2017-10-20
 */
public class Bootstrap {

    private static String packageName;
    private static String entityDirectoryPath;
    private static String daoDirectoryPath;
    private static String daoImplDirectoryPath;

    private static void init() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("my_generate");
        packageName = bundle.getString("packageName");
        entityDirectoryPath = packageName+"\\myentity";
        daoDirectoryPath = packageName+"\\mydao";
        daoImplDirectoryPath = packageName+"\\mydao\\impl";
    }

    public static void main(String[] args) throws IOException {
        init();
        generateEntity();
        //generateDAOAndImpl();

    }

    private static void generateEntity() throws IOException {
        File entityDirectory = new File(entityDirectoryPath);
        if(!entityDirectory.exists()){
            entityDirectory.mkdirs();
        }
        //获取所有的表名
        //List<Map<String, Object>> list = queryData("SELECT TABLE_NAME FROM tabs");
        //for(Map<String, Object> map : list){
        //    String tableName = (String) map.get("table_name");
        //    //转换成规范的java类名
        //    String className = change2JavaClassName(tableName);
        //    String classFileName = className + ".java";
        //    File classFile = new File(entityDirectory,"\\"+classFileName);
        //    classFile.createNewFile();
        //    writeEntity(classFile,tableName);
        //
        //}


    }

    private static void writeEntity(File file, String tableName) throws IOException {
        String className = file.getName().substring(0,file.getName().indexOf(".java"));
        InputStreamReader isr = new FileReader("src\\main\\resources\\entity_template.txt");
        BufferedReader br = new BufferedReader(isr);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        //获取所有字段
        //List<Map<String,Object>> columnList = listColumnByTableName(tableName);
        List<Map<String,Object>> columnList = null;
        StringBuilder fieldContent = new StringBuilder();
        for(Map<String,Object> column : columnList){
            String cname = (String) column.get("cname");
            if(column.get("coltype").equals("VARCHAR2")){
                // TODO: 2017/10/20/020
                fieldContent.append("private String "+"");
            }else if (column.get("coltype").equals("DATE")){
                fieldContent.append("private Date "+"");
            }else if (column.get("coltype").equals("NUMBER")){
                // TODO: 2017/10/20/020 判断有没有小数点
                if(true){
                    fieldContent.append("private Integer "+"");
                }else {
                    fieldContent.append("private Double "+"");
                }
            }
        }

        String str = null;
        while((str = br.readLine()) != null){
            if(str.contains("$TABLE_NAME")){
                str = str.replace("$TABLE_NAME",tableName);
            }
            if(str.contains("$CLASS_NAME")){
                str = str.replace("$CLASS_NAME",className);
            }
            if(str.contains("$CONTENT")){
                str = str.replace("$CONTENT",fieldContent.toString());
            }
            if(str.contains("$DATE")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                str = str.replace("$DATE",dateStr);
            }
            bw.write(str);
            bw.newLine();
            bw.flush();
        }
        bw.close();
        br.close();
    }

    //private static List<Map<String,Object>> listColumnByTableName(String tableName) {
    //    return queryData("select cname , coltype from col where tname="+tableName);
    //}

    private static String change2JavaClassName(String tableName) {
        int count = 0;
        //不改变大小写的位置
        int nochangeCaseIndex = 0;
        StringBuilder result = new StringBuilder();
        for(int i = 0 ; i < tableName.length() ; i ++ ){

            if(tableName.charAt(i) != '_'){
                if(i == nochangeCaseIndex){
                    result.append(tableName.charAt(i));
                }else {
                    result.append((char) (tableName.charAt(i)+32));
                }
            }else {
                nochangeCaseIndex = i + 1;
            }
        }
        //去除_
        return  result.toString().replace("_","");

    }

    private static void generateDAOAndImpl() throws IOException {
        File entityDirectory = new File(entityDirectoryPath);
        File daoDirectory = new File(daoDirectoryPath);
        File daoImplDirectory = new File(daoImplDirectoryPath);
        if(!daoDirectory.exists()){
            daoDirectory.mkdirs();
        }
        if(!daoImplDirectory.exists()){
            daoImplDirectory.mkdirs();
        }

        File[] files = entityDirectory.listFiles();
        for(File file : files){

            String daoFileName = file.getName().substring(0,file.getName().indexOf(".java")) + "DAO.java";
            String daoImplFileName = file.getName().substring(0,file.getName().indexOf(".java")) + "DAOImpl.java";
            File daoFile = new File(daoDirectory,"\\"+daoFileName);
            File daoImplFile = new File(daoDirectory,"\\impl\\"+daoImplFileName);
            daoFile.createNewFile();
            daoImplFile.createNewFile();
            writeDAO(daoFile);
            writeDAOImpl(daoImplFile);

        }
    }

    public static void writeDAO(File file) throws IOException {
        String classDAOName = file.getName().substring(0,file.getName().indexOf(".java"));
        String className = classDAOName.substring(0,classDAOName.indexOf("DAO"));
        InputStreamReader isr = new FileReader("src\\main\\resources\\dao_template.txt");
        BufferedReader br = new BufferedReader(isr);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        String str = null;
        while((str = br.readLine()) != null){
            if(str.contains("$CLASS_DAO_NAME")){
                str = str.replace("$CLASS_DAO_NAME",classDAOName);
            }
            if(str.contains("$CLASS_NAME")){
                str = str.replace("$CLASS_NAME",className);
            }
            if(str.contains("$DATE")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                str = str.replace("$DATE",dateStr);
            }
            bw.write(str);
            bw.newLine();
            bw.flush();
        }
        bw.close();
        br.close();
    }

    public static void writeDAOImpl(File file) throws IOException {
        String classDAOImplName = file.getName().substring(0,file.getName().indexOf(".java"));
        String classDAOName = classDAOImplName.substring(0,classDAOImplName.indexOf("Impl"));
        String className = classDAOName.substring(0,classDAOName.indexOf("DAO"));
        InputStreamReader isr = new FileReader("src\\main\\resources\\dao_impl_template.txt");
        BufferedReader br = new BufferedReader(isr);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        String str = null;
        while((str = br.readLine()) != null){
            if(str.contains("$CLASS_DAO_NAME")){
                str = str.replace("$CLASS_DAO_NAME",classDAOName);
            }
            if(str.contains("$CLASS_DAO_IMPL_NAME")){
                str = str.replace("$CLASS_DAO_IMPL_NAME",classDAOImplName);
            }
            if(str.contains("$CLASS_NAME")){
                str = str.replace("$CLASS_NAME",className);
            }
            if(str.contains("$DATE")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                str = str.replace("$DATE",dateStr);
            }
            bw.write(str);
            bw.newLine();
            bw.flush();
        }
        bw.close();
        br.close();
    }

    /**
     * 导出数据
     *
     * @param sql
     * @return
     */
    //private static List<Map<String, Object>> queryData(String sql) {
    //    Connection connection = JDBCUtil.getConnection
    //            ("oracle", "//btzh.net:1521/orcl",
    //            "bm_zfjg", "bmtech");
    //    if (connection == null) {
    //        throw new RuntimeException("源数据源无法连接");
    //    }
    //    PreparedStatement statement = null;
    //    ResultSet resultSet = null;
    //    try {
    //        statement = connection.prepareStatement(sql);
    //        resultSet = statement.executeQuery();
    //        return listData(resultSet);
    //    } catch (SQLException e) {
    //        throw new RuntimeException("提取数据SQL错误，错误信息：" + e.getMessage());
    //    } finally {
    //        JDBCUtil.closeAll(connection, statement, resultSet);
    //    }
    //}


    /**
     * 处理ResultSet数据
     *
     * @param resultSet
     * @return
     */
    private static List<Map<String, Object>> listData(ResultSet resultSet) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= count; i++) {
                    String columnLable = metaData.getColumnLabel(i).toLowerCase();
                    Object value = resultSet.getObject(columnLable);
                    map.put(columnLable, value);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            //throw new DepBusinessException("提取数据SQL错误，错误信息：" + e.getMessage());
        }

        return list;
    }
}
