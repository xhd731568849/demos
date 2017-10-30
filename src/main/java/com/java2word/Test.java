package com.java2word;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xhd
 * @date 2017-10-30
 */
public class Test {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("pwd","密码");
        map.put("addr","地址");
        createDoc(map,"resume");
    }



    private static Configuration configuration = null ;
    private static Map<String, Template> allTemplates = null ;

    static {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(Test.class, "/");
        allTemplates = new HashMap<String, Template>();   // Java 7 钻石语法
        try {
            allTemplates.put("resume", configuration.getTemplate("ftl/template.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Test() {
        throw new AssertionError();
    }

    public static File createDoc(Map<?, ?> dataMap, String type) {
        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File f = new File(name);
        Template t = allTemplates.get(type);
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }


//    private Configuration configuration = ;
//
//    public WordTest(){
//        configuration = new Configuration();
//        configuration.setDefaultEncoding("UTF-8");
//    }
//
//    public static void main(String[] args) {
//        WordTest test = new WordTest();
//        test.createWord();
//    }
//
//    public void createWord(){
//        Map<String,Object> dataMap=new HashMap<String,Object>();
//        getData(dataMap);
//        configuration.setClassForTemplateLoading(this.getClass(), "/com");  //FTL文件所存在的位置
//        Template t=;
//        try {
//            t = configuration.getTemplate("wordModel.ftl"); //文件名
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File outFile = new File("O:/outFilessa"+Math.random()*10000+".doc");
//        Writer out = ;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
//
//        try {
//            t.process(dataMap, out);
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getData(Map<String, Object> dataMap) {
//        dataMap.put("title", "标题");
//        dataMap.put("year", "2012");
//        dataMap.put("month", "2");
//        dataMap.put("day", "13");
//        dataMap.put("auditor", "唐鑫");
//        dataMap.put("phone", "13020265912");
//        dataMap.put("weave", "占文涛");
////      dataMap.put("number", 1);
////      dataMap.put("content", "内容"+2);
//
//        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        for (int i = 0; i < 10; i++) {
//            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("number", i);
//            map.put("content", "内容"+i);
//            list.add(map);
//        }
//
//
//        dataMap.put("list", list);
//    }
}
